package com.example.medicalmod.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Consommable de soin generique, configurable pour occuper un creneau precis :
 *  - healAmount        : soin instantane (0 = aucun, item purement regen/nettoyant)
 *  - regenDurationTicks: Regeneration sur la duree (0 = aucune)
 *  - useTimeTicks      : duree d'utilisation (court = spammable, long = risque en combat)
 *  - clearsDebuffs     : retire les effets negatifs classiques (antipoison)
 *
 * Ne soigne AUCUNE blessure du mod : c'est le role exclusif de MedicalItem.
 */
public class HealingItem extends Item {
    private final float healAmount;
    private final int regenDurationTicks;
    private final int regenAmplifier;
    private final int useTimeTicks;
    private final boolean clearsDebuffs;

    public HealingItem(Settings settings, float healAmount, int regenDurationTicks,
                       int regenAmplifier, int useTimeTicks, boolean clearsDebuffs) {
        super(settings);
        this.healAmount = healAmount;
        this.regenDurationTicks = regenDurationTicks;
        this.regenAmplifier = regenAmplifier;
        this.useTimeTicks = useTimeTicks;
        this.clearsDebuffs = clearsDebuffs;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        boolean hurt = user.getHealth() < user.getMaxHealth();
        // Le nettoyant est utile meme a pleine vie (ex : empoisonne mais full PV).
        boolean debuffToClear = this.clearsDebuffs && hasHarmfulEffect(user);

        if (hurt || debuffToClear) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            if (this.healAmount > 0.0F) {
                user.heal(this.healAmount);
            }
            if (this.regenDurationTicks > 0) {
                user.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.REGENERATION, this.regenDurationTicks, this.regenAmplifier));
            }
            if (this.clearsDebuffs) {
                clearHarmfulEffects(user);
            }
        }
        if (user instanceof PlayerEntity player && !player.getAbilities().creativeMode) {
            stack.decrement(1);
        }
        return stack;
    }

    private static boolean hasHarmfulEffect(LivingEntity user) {
        for (StatusEffectInstance instance : user.getStatusEffects()) {
            if (instance.getEffectType().getCategory() == StatusEffectCategory.HARMFUL) {
                return true;
            }
        }
        return false;
    }

    /** Retire les effets nefastes en gardant les effets positifs (contrairement au lait). */
    private static void clearHarmfulEffects(LivingEntity user) {
        List<StatusEffect> toRemove = new ArrayList<>();
        for (StatusEffectInstance instance : user.getStatusEffects()) {
            if (instance.getEffectType().getCategory() == StatusEffectCategory.HARMFUL) {
                toRemove.add(instance.getEffectType());
            }
        }
        for (StatusEffect effect : toRemove) {
            user.removeStatusEffect(effect);
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return this.useTimeTicks;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return this.healAmount > 0.0F ? UseAction.EAT : UseAction.BOW;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.medicalmod.desc." + Registries.ITEM.getId(this).getPath())
                .formatted(Formatting.GRAY));
        if (this.healAmount > 0.0F) {
            tooltip.add(Text.translatable("tooltip.medicalmod.heal.instant").formatted(Formatting.GRAY));
        }
        if (this.regenDurationTicks > 0) {
            tooltip.add(Text.translatable("tooltip.medicalmod.heal.regen").formatted(Formatting.GRAY));
        }
        if (this.clearsDebuffs) {
            tooltip.add(Text.translatable("tooltip.medicalmod.heal.cleanse").formatted(Formatting.AQUA));
        }
        if (this.useTimeTicks >= 48) {
            tooltip.add(Text.translatable("tooltip.medicalmod.heal.slow").formatted(Formatting.DARK_GRAY));
        }
        tooltip.add(Text.translatable("tooltip.medicalmod.use_hint").formatted(Formatting.DARK_GRAY));
    }
}
