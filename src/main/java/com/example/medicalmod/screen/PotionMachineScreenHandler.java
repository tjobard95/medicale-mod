package com.example.medicalmod.screen;

import com.example.medicalmod.block.entity.PotionMachineBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class PotionMachineScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    /** Constructeur client. */
    public PotionMachineScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(3), new ArrayPropertyDelegate(2));
    }

    /** Constructeur serveur. */
    public PotionMachineScreenHandler(int syncId, PlayerInventory playerInventory,
                                      Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.POTION_MACHINE, syncId);
        checkSize(inventory, 3);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        inventory.onOpen(playerInventory.player);

        // Entree 1 : bouteille d'eau
        this.addSlot(new Slot(inventory, 0, 44, 20) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.POTION);
            }
        });
        // Entree 2 : concentre nutritif
        this.addSlot(new Slot(inventory, 1, 44, 50) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return PotionMachineBlockEntity.isValidIngredient(stack);
            }
        });
        // Sortie : lecture seule
        this.addSlot(new Slot(inventory, 2, 116, 35) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });

        // Inventaire du joueur
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        this.addProperties(propertyDelegate);
    }

    /** Avancement de la fabrication, 0.0 a 1.0 (pour la barre de l'ecran). */
    public float getProgressRatio() {
        int progress = this.propertyDelegate.get(0);
        int total = this.propertyDelegate.get(1);
        return total == 0 ? 0.0F : (float) progress / total;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack stack = slot.getStack();
            result = stack.copy();

            if (index < 3) {
                if (!this.insertItem(stack, 3, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(stack, 0, 3, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return result;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
