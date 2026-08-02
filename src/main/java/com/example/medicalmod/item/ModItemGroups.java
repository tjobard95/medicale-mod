package com.example.medicalmod.item;

import com.example.medicalmod.MedicalMod;
import com.example.medicalmod.block.ModBlocks;
import com.example.medicalmod.potion.ModPotions;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final ItemGroup COTTON_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(MedicalMod.MOD_ID, "cotton_group"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.COTTON))
                    .displayName(Text.translatable("itemgroup.medicalmod"))
                    .entries((displayContext, entries) -> {
                        // Coton
                        entries.add(ModItems.COTTON_SEEDS);
                        entries.add(ModItems.COTTON);
                        entries.add(ModItems.THREAD);
                        entries.add(ModItems.CLOTH);
                        // Plantes medicinales
                        entries.add(ModItems.ALOE_SEEDS);
                        entries.add(ModItems.ALOE_LEAF);
                        entries.add(ModItems.CHAMOMILE_SEEDS);
                        entries.add(ModItems.CHAMOMILE_FLOWER);
                        entries.add(ModItems.CALENDULA_SEEDS);
                        entries.add(ModItems.CALENDULA_FLOWER);
                        // Produits transformes
                        entries.add(ModItems.ANTISEPTIC);
                        entries.add(ModItems.ALOE_GEL);
                        entries.add(ModItems.SALVE);
                        entries.add(ModItems.HERBAL_MEDICINE);
                        // Soins + blessures
                        entries.add(ModItems.SYRINGE);
                        entries.add(ModItems.BANDAGE);
                        entries.add(ModItems.SPLINT);
                        entries.add(ModItems.MEDICINE);
                        entries.add(ModItems.PAINKILLER);
                        entries.add(ModItems.MEDKIT);
                        entries.add(ModBlocks.POTION_MACHINE);
                        // Parachute
                        entries.add(ModItems.PARACHUTE);
                        // Potions du mod (fabriquees uniquement au Distillateur nutritif).
                        // Une potion n'est pas un Item : c'est un ItemStack de minecraft:potion
                        // auquel on applique la potion -> il faut les ajouter explicitement.
                        addPotionForms(entries, ModPotions.SATURATION);
                        addPotionForms(entries, ModPotions.ADRENALINE);
                        addPotionForms(entries, ModPotions.FIRST_AID);
                    })
                    .build());

    /** Ajoute une potion sous ses 3 formes : a boire, en jet, persistante. */
    private static void addPotionForms(ItemGroup.Entries entries, Potion potion) {
        entries.add(PotionUtil.setPotion(new ItemStack(Items.POTION), potion));
        entries.add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), potion));
        entries.add(PotionUtil.setPotion(new ItemStack(Items.LINGERING_POTION), potion));
    }

    public static void registerItemGroups() {
        MedicalMod.LOGGER.info("Enregistrement des groupes d'items de Medical Mod");
    }
}
