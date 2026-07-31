package com.example.medicalmod.item;

import com.example.medicalmod.injury.InjuryManager;
import com.example.medicalmod.injury.InjuryType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Item de soin qui guerit une blessure precise (attelle, pansement, medicament...).
 * Peut aussi rendre des PV et de la regeneration comme HealingItem.
 */
public class MedicalItem extends Item {
    private final InjuryType cures;
    private final float healAmount;
    private final int regenDurationTicks;
    private final int regenAmplifier;
    private final int useTimeTicks;

    public MedicalItem(Settings settings, InjuryType cures, float healAmount,
                       int regenDurationTicks, int regenAmplifier, int useTimeTicks) {
        super(settings);
        this.cures = cures;
        this.healAmount = healAmount;
        this.regenDurationTicks = regenDurationTicks;
        this.regenAmplifier = regenAmplifier;
        this.useTimeTicks = useTimeTicks;
    }

    public InjuryType getCuredInjury() {
        return this.cures;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        boolean injured = InjuryManager.get(user).has(this.cures);
        boolean hurt = user.getHealth() < user.getMaxHealth();

        if (!injured && !hurt) {
            if (!world.isClient) {
                user.sendMessage(Text.translatable("message.medicalmod.nothing_to_treat")
                        .formatted(Formatting.GRAY), true);
            }
            return TypedActionResult.fail(stack);
        }

        user.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player) {
            InjuryManager.cure(player, this.cures);

            if (this.healAmount > 0.0F) {
                player.heal(this.healAmount);
            }
            if (this.regenDurationTicks > 0) {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.REGENERATION, this.regenDurationTicks, this.regenAmplifier));
            }
            world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_HONEY_BOTTLE_DRINK,
                    SoundCategory.PLAYERS, 0.7F, 1.4F);
        }

        if (user instanceof PlayerEntity player && !player.getAbilities().creativeMode) {
            stack.decrement(1);
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return this.useTimeTicks;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.medicalmod.cures", this.cures.getDisplayName())
                .formatted(Formatting.GRAY));
        if (this.healAmount > 0.0F) {
            tooltip.add(Text.translatable("tooltip.medicalmod.heal.also_life").formatted(Formatting.GRAY));
        }
        tooltip.add(Text.translatable("tooltip.medicalmod.use_hint").formatted(Formatting.DARK_GRAY));
    }
}
