package com.example.medicalmod.screen;

import com.example.medicalmod.MedicalMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {

    public static final ScreenHandlerType<PotionMachineScreenHandler> POTION_MACHINE =
            Registry.register(Registries.SCREEN_HANDLER,
                    new Identifier(MedicalMod.MOD_ID, "potion_machine"),
                    new ScreenHandlerType<>(PotionMachineScreenHandler::new,
                            net.minecraft.resource.featuretoggle.FeatureFlags.VANILLA_FEATURES));

    public static void registerScreenHandlers() {
        MedicalMod.LOGGER.info("Enregistrement des interfaces de Medical Mod");
    }
}
