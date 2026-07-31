package com.example.medicalmod.item;

import com.example.medicalmod.inventory.BackSlotAccess;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Le parachute ne se porte plus dans la case du plastron : il a sa propre case
 * "dos", a droite du mannequin de l'inventaire. On garde donc son armure.
 *
 * - Enchantable (table + enclume + livres)
 * - Renommable a l'enclume (comportement vanilla de tout item)
 * - Reparable avec du tissu
 */
public class ParachuteItem extends Item {

    public ParachuteItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    @Override
    public int getEnchantability() {
        return 12;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(ModItems.CLOTH);
    }

    /** Clic droit : equipe / echange le parachute dans la case du dos. */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient) {
            SimpleInventory back = ((BackSlotAccess) user).medicalmod$getBackInventory();
            ItemStack current = back.getStack(0);

            if (!current.isEmpty()) {
                // Un parachute est deja dans le dos : on l'echange s'il y a la place.
                if (!user.getInventory().insertStack(current.copy())) {
                    user.sendMessage(Text.translatable("message.medicalmod.inventory_full")
                            .formatted(Formatting.RED), true);
                    return TypedActionResult.fail(stack);
                }
            }

            back.setStack(0, stack.copy());
            user.setStackInHand(hand, ItemStack.EMPTY);
            ((BackSlotAccess) user).medicalmod$markDirty();
        }

        world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
                SoundCategory.PLAYERS, 1.0F, 1.0F);
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.medicalmod.parachute.equip").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.medicalmod.parachute.use").formatted(Formatting.DARK_GRAY));
    }
}
