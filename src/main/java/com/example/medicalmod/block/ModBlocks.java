package com.example.medicalmod.block;

import com.example.medicalmod.MedicalMod;
import com.example.medicalmod.item.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block COTTON_CROP = registerBlock("cotton_crop",
            new CottonCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT)));

    // Plantes medicinales
    public static final Block ALOE_VERA = registerBlock("aloe_vera",
            new MedicinalCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT), () -> ModItems.ALOE_SEEDS));
    public static final Block CHAMOMILE = registerBlock("chamomile",
            new MedicinalCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT), () -> ModItems.CHAMOMILE_SEEDS));
    public static final Block CALENDULA = registerBlock("calendula",
            new MedicinalCropBlock(FabricBlockSettings.copyOf(Blocks.WHEAT), () -> ModItems.CALENDULA_SEEDS));

    /**
     * Distillateur nutritif : machine EXCLUSIVE.
     * Aucune recette de craft -> uniquement via /give (OP) ou creatif.
     */
    public static final Block POTION_MACHINE = registerBlockWithItem("potion_machine",
            new PotionMachineBlock(FabricBlockSettings.copyOf(Blocks.BLAST_FURNACE)
                    .strength(-1.0F, 3600000.0F)
                    // copyOf recopie la luminance du haut fourneau, qui vaut
                    // createLightLevelFromLitBlockState(13) et lit donc la propriete LIT.
                    // Ce bloc ne declare pas LIT : sans cette neutralisation, la lambda
                    // est evaluee a la construction des BlockState et fait planter le jeu.
                    .luminance(state -> 0)));

    private static Block registerBlockWithItem(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(MedicalMod.MOD_ID, name),
                new net.minecraft.item.BlockItem(block, new net.minecraft.item.Item.Settings()));
        return registerBlock(name, block);
    }

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(MedicalMod.MOD_ID, name), block);
    }

    public static void registerModBlocks() {
        MedicalMod.LOGGER.info("Enregistrement des blocs de Medical Mod");
    }
}
