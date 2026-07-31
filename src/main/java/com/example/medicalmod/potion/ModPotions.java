package com.example.medicalmod.potion;

import com.example.medicalmod.MedicalMod;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Les 3 potions du mod. Aucune n'a de recette d'alambic :
 * elles se fabriquent UNIQUEMENT dans le Distillateur nutritif,
 * un bloc sans recette de craft (voir ModBlocks).
 */
public class ModPotions {

    /** Saturation : remplit la faim d'un coup. */
    public static final Potion SATURATION = register("saturation",
            new Potion("saturation", new StatusEffectInstance(StatusEffects.SATURATION, 1, 19)));

    /**
     * Adrenaline : Force + Rapidite + Celerite pendant 45 s, PUIS contrecoup.
     * C'est ici qu'on retrouve l'effet de celerite (vitesse de minage).
     */
    public static final Potion ADRENALINE = register("adrenaline",
            new Potion("adrenaline",
                    new StatusEffectInstance(StatusEffects.STRENGTH, 900, 0, false, true, true),
                    new StatusEffectInstance(StatusEffects.SPEED, 900, 0, false, true, true),
                    new StatusEffectInstance(StatusEffects.HASTE, 900, 1, false, true, true),
                    new StatusEffectInstance(ModEffects.ADRENALINE, 900, 0, false, false, false)));

    /** Serum de premiers secours : soigne toutes les blessures + gros soin. */
    public static final Potion FIRST_AID = register("first_aid",
            new Potion("first_aid",
                    new StatusEffectInstance(ModEffects.FIRST_AID, 1, 0, false, false, true),
                    new StatusEffectInstance(StatusEffects.REGENERATION, 100, 1, false, true, true),
                    new StatusEffectInstance(StatusEffects.ABSORPTION, 1200, 0, false, true, true)));

    private static Potion register(String name, Potion potion) {
        return Registry.register(Registries.POTION, new Identifier(MedicalMod.MOD_ID, name), potion);
    }

    public static void registerModPotions() {
        MedicalMod.LOGGER.info("Enregistrement des potions");
    }
}
