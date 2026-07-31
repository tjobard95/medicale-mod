package com.example.medicalmod.block;

import com.example.medicalmod.item.ModItems;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;

public class CottonCropBlock extends CropBlock {
    public CottonCropBlock(Settings settings) {
        super(settings);
    }

    // L'item utilise pour (re)planter la culture.
    @Override
    protected ItemConvertible getSeedsItem() {
        return ModItems.COTTON_SEEDS;
    }
}
