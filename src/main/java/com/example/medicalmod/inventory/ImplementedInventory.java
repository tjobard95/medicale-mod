package com.example.medicalmod.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

/**
 * Implementation minimale de Inventory basee sur une DefaultedList.
 * Evite de reecrire les memes 10 methodes dans chaque bloc a inventaire.
 */
public interface ImplementedInventory extends Inventory {

    DefaultedList<ItemStack> getItems();

    @Override
    default int size() {
        return this.getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (ItemStack stack : this.getItems()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default ItemStack getStack(int slot) {
        return this.getItems().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int amount) {
        ItemStack result = net.minecraft.inventory.Inventories.splitStack(this.getItems(), slot, amount);
        if (!result.isEmpty()) {
            this.markDirty();
        }
        return result;
    }

    @Override
    default ItemStack removeStack(int slot) {
        ItemStack result = net.minecraft.inventory.Inventories.removeStack(this.getItems(), slot);
        this.markDirty();
        return result;
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        this.getItems().set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
        this.markDirty();
    }

    @Override
    default void clear() {
        this.getItems().clear();
        this.markDirty();
    }

    @Override
    default void markDirty() {
        // surcharge par le BlockEntity
    }

    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
