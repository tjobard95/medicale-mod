package com.example.medicalmod.config;

import com.example.medicalmod.MedicalMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Reglages du systeme de blessures : config/medicalmod.json
 *
 * Toutes les probabilites sont entre 0.0 (jamais) et 1.0 (toujours).
 * Modifiable a chaud avec /medicalmod reload.
 */
public class MedicalModConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static MedicalModConfig INSTANCE;

    // ---------------------------------------------------------- global

    /** Coupe completement le systeme de blessures. */
    public boolean injuriesEnabled = true;

    /** Multiplicateur applique a TOUTES les probabilites. 0.5 = deux fois moins de blessures. */
    public float globalChanceMultiplier = 1.0F;

    /** Nombre max de blessures simultanees (evite d'accumuler les 4 d'un coup). */
    public int maxSimultaneousInjuries = 2;

    /** Delai minimum entre deux blessures, en secondes (repit apres un coup dur). */
    public int injuryCooldownSeconds = 30;

    // ------------------------------------------------------- jambe cassee

    /** Degats de chute en dessous desquels aucune fracture n'est possible. */
    public float fallDamageThreshold = 8.0F;
    /** Probabilite ajoutee par point de degat au-dela du seuil. */
    public float fallChancePerPoint = 0.04F;
    /** Plafond de probabilite pour la fracture. */
    public float fallChanceMax = 0.60F;

    // --------------------------------------------------------- saignement

    /** Degats minimum pour qu'un saignement soit possible. */
    public float bleedDamageThreshold = 6.0F;
    public float bleedChance = 0.07F;
    /** Secondes entre deux ticks de degats de saignement. */
    public int bleedIntervalSeconds = 5;
    public float bleedDamage = 1.0F;

    // ---------------------------------------------------------- infection

    public float infectionChance = 0.04F;

    // --------------------------------------------------------- commotion

    /** Degats de chute a partir desquels une commotion est possible. */
    public float concussionFallThreshold = 16.0F;
    public float concussionFallChance = 0.10F;
    /** Explosions, enclumes, blocs qui tombent. */
    public float concussionImpactChance = 0.10F;

    // ------------------------------------------------------------ acces

    public static MedicalModConfig get() {
        if (INSTANCE == null) {
            load();
        }
        return INSTANCE;
    }

    /** Applique le multiplicateur global et borne le resultat entre 0 et 1. */
    public float scaled(float chance) {
        float result = chance * this.globalChanceMultiplier;
        return Math.max(0.0F, Math.min(1.0F, result));
    }

    public int injuryCooldownTicks() {
        return Math.max(0, this.injuryCooldownSeconds) * 20;
    }

    public int bleedIntervalTicks() {
        return Math.max(1, this.bleedIntervalSeconds) * 20;
    }

    // ----------------------------------------------------- chargement

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("medicalmod.json");
    }

    public static void load() {
        Path file = path();
        if (Files.exists(file)) {
            try {
                INSTANCE = GSON.fromJson(Files.readString(file), MedicalModConfig.class);
            } catch (Exception e) {
                MedicalMod.LOGGER.error("medicalmod.json illisible, valeurs par defaut utilisees", e);
            }
        }
        if (INSTANCE == null) {
            INSTANCE = new MedicalModConfig();
        }
        save();
    }

    public static void save() {
        try {
            Path file = path();
            Files.createDirectories(file.getParent());
            Files.writeString(file, GSON.toJson(get()));
        } catch (IOException e) {
            MedicalMod.LOGGER.error("Impossible d'ecrire medicalmod.json", e);
        }
    }
}
