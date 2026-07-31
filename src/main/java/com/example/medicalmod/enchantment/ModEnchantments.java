package com.example.medicalmod.enchantment;

import com.example.medicalmod.MedicalMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModEnchantments {

    // Les 3 premiers sont "normaux" : table d'enchantement + livres.
    /** Planeur : plus de glisse horizontale, descente plus maitrisee. (I a III) */
    public static final Enchantment GLIDE = register("glide",
            new ParachuteEnchantment(Enchantment.Rarity.COMMON, 3, false));

    /** Atterrissage en douceur : annule les degats residuels, divise le risque de fracture. (I a II) */
    public static final Enchantment SOFT_LANDING = register("soft_landing",
            new ParachuteEnchantment(Enchantment.Rarity.UNCOMMON, 2, false));

    /** Toile renforcee : la durabilite s'use deux fois moins vite par niveau. (I a III) */
    public static final Enchantment REINFORCED_CANVAS = register("reinforced_canvas",
            new ParachuteEnchantment(Enchantment.Rarity.UNCOMMON, 3, false));

    /**
     * Deploiement automatique : plus besoin d'appuyer sur SHIFT. (I)
     * ENCHANTEMENT TRESOR : introuvable a la table, uniquement en livre
     * trouve dans un coffre, peche, ou achete a un bibliothecaire.
     */
    public static final Enchantment AUTO_DEPLOY = register("auto_deploy",
            new ParachuteEnchantment(Enchantment.Rarity.RARE, 1, true));

    public static final List<Enchantment> PARACHUTE_ENCHANTMENTS =
            List.of(GLIDE, SOFT_LANDING, REINFORCED_CANVAS, AUTO_DEPLOY);

    private static Enchantment register(String name, Enchantment enchantment) {
        return Registry.register(Registries.ENCHANTMENT,
                new Identifier(MedicalMod.MOD_ID, name), enchantment);
    }

    public static void registerModEnchantments() {
        MedicalMod.LOGGER.info("Enregistrement des enchantements de Medical Mod");
    }
}
