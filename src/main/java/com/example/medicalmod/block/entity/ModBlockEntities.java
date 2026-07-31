package com.example.medicalmod.block.entity;

import com.example.medicalmod.MedicalMod;
import com.example.medicalmod.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static final BlockEntityType<PotionMachineBlockEntity> POTION_MACHINE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(MedicalMod.MOD_ID, "potion_machine"),
                    FabricBlockEntityTypeBuilder.create(
                            PotionMachineBlockEntity::new, ModBlocks.POTION_MACHINE).build());

    public static void registerBlockEntities() {
        MedicalMod.LOGGER.info("Enregistrement des block entities de Medical Mod");
    }
}
