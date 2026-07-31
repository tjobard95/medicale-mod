package com.example.medicalmod.potion;

import com.example.medicalmod.MedicalMod;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEffects {

    public static final StatusEffect ADRENALINE = register("adrenaline", new AdrenalineEffect());
    public static final StatusEffect FIRST_AID = register("first_aid", new FirstAidEffect());

    private static StatusEffect register(String name, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(MedicalMod.MOD_ID, name), effect);
    }

    public static void registerModEffects() {
        MedicalMod.LOGGER.info("Enregistrement des effets");
    }
}
