package com.example.medicalmod.inventory;

import com.example.medicalmod.item.ParachuteItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

/** Case dediee au parachute, ajoutee a cote du mannequin dans l'inventaire. */
public class BackSlot extends Slot {
    public BackSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof ParachuteItem;
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }
}
