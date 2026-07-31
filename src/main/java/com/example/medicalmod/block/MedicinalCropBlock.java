package com.example.medicalmod.block;

import net.minecraft.block.CropBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;

import java.util.function.Supplier;

// Culture medicinale generique : meme logique que le ble, mais on lui passe
// l'item-graine a utiliser (evite de creer une classe par plante).
public class MedicinalCropBlock extends CropBlock {
    private final Supplier<Item> seedSupplier;

    public MedicinalCropBlock(Settings settings, Supplier<Item> seedSupplier) {
        super(settings);
        this.seedSupplier = seedSupplier;
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return seedSupplier.get();
    }
}
