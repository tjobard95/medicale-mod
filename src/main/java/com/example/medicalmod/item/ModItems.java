package com.example.medicalmod.item;

import com.example.medicalmod.MedicalMod;
import com.example.medicalmod.block.ModBlocks;
import com.example.medicalmod.injury.InjuryType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    // ---------- Coton ----------
    public static final Item COTTON = registerItem("cotton",
            new Item(new FabricItemSettings()));
    public static final Item COTTON_SEEDS = registerItem("cotton_seeds",
            new AliasedBlockItem(ModBlocks.COTTON_CROP, new FabricItemSettings()));
    public static final Item THREAD = registerItem("thread",
            new Item(new FabricItemSettings()));
    public static final Item CLOTH = registerItem("cloth",
            new Item(new FabricItemSettings()));
    public static final Item SYRINGE = registerItem("syringe",
            new Item(new FabricItemSettings().maxCount(16)));

    // ---------- Plantes medicinales : graines ----------
    public static final Item ALOE_SEEDS = registerItem("aloe_seeds",
            new AliasedBlockItem(ModBlocks.ALOE_VERA, new FabricItemSettings()));
    public static final Item CHAMOMILE_SEEDS = registerItem("chamomile_seeds",
            new AliasedBlockItem(ModBlocks.CHAMOMILE, new FabricItemSettings()));
    public static final Item CALENDULA_SEEDS = registerItem("calendula_seeds",
            new AliasedBlockItem(ModBlocks.CALENDULA, new FabricItemSettings()));

    // ---------- Recoltes ----------
    public static final Item ALOE_LEAF = registerItem("aloe_leaf",
            new Item(new FabricItemSettings()));
    public static final Item CHAMOMILE_FLOWER = registerItem("chamomile_flower",
            new Item(new FabricItemSettings()));
    public static final Item CALENDULA_FLOWER = registerItem("calendula_flower",
            new Item(new FabricItemSettings()));

    // ---------- Produits transformes ----------
    public static final Item ANTISEPTIC = registerItem("antiseptic",
            new Item(new FabricItemSettings().maxCount(16)));
    // Gel d'aloes : appoint instantane, rapide et bon marche (usage court, spammable).
    public static final Item ALOE_GEL = registerItem("aloe_gel",
            new HealingItem(new FabricItemSettings().maxCount(16), 2.0F, 0, 0, 16, false));
    // Pommade : regeneration sur la duree, AUCUN soin instantane (a utiliser hors combat).
    public static final Item SALVE = registerItem("salve",
            new HealingItem(new FabricItemSettings().maxCount(16), 0.0F, 200, 0, 32, false));
    // Remede a base de plantes : petit soin + retire les effets negatifs (antipoison).
    public static final Item HERBAL_MEDICINE = registerItem("herbal_medicine",
            new HealingItem(new FabricItemSettings().maxCount(16), 2.0F, 0, 0, 32, true));

    // ---------- Soins de blessures ----------
    // Pansement : arrete le saignement (et debloque donc la regeneration).
    public static final Item BANDAGE = registerItem("bandage",
            new MedicalItem(new FabricItemSettings().maxCount(16),
                    InjuryType.BLEEDING, 3.0F, 0, 0, 40));
    // Attelle : repare une jambe cassee.
    public static final Item SPLINT = registerItem("splint",
            new MedicalItem(new FabricItemSettings().maxCount(8),
                    InjuryType.BROKEN_LEG, 2.0F, 0, 0, 60));
    // Medicament : traite l'infection.
    public static final Item MEDICINE = registerItem("medicine",
            new MedicalItem(new FabricItemSettings().maxCount(16),
                    InjuryType.INFECTION, 2.0F, 100, 0, 32));
    // Antidouleur : traite la commotion.
    public static final Item PAINKILLER = registerItem("painkiller",
            new MedicalItem(new FabricItemSettings().maxCount(16),
                    InjuryType.CONCUSSION, 1.0F, 60, 0, 24));
    // Kit de soin : soigne beaucoup de PV mais AUCUNE blessure.
    // Kit de soin : gros soin d'urgence, mais long a appliquer (risque en plein combat) et cher.
    public static final Item MEDKIT = registerItem("medkit",
            new HealingItem(new FabricItemSettings().maxCount(16), 8.0F, 100, 0, 50, false));

    // ---------- Parachute ----------
    public static final Item PARACHUTE = registerItem("parachute",
            new ParachuteItem(new FabricItemSettings().maxCount(1).maxDamage(180)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MedicalMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        MedicalMod.LOGGER.info("Enregistrement des items de Medical Mod");
    }
}
