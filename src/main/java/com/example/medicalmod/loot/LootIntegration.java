package com.example.medicalmod.loot;

import com.example.medicalmod.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.Set;

/**
 * Ajoute des livres d'enchantement de parachute dans quelques coffres de
 * structures : c'est le chemin de "trouvaille" concret et garanti.
 *
 * A cote de ca, vanilla fait deja apparaitre ces enchantements :
 *  - sur les livres vendus par les bibliothecaires (tresor inclus),
 *  - a la peche (categorie tresor),
 *  - dans les livres aleatoires d'autres coffres.
 * Cette injection ne fait qu'ajouter un point de decouverte fiable et thematique.
 */
public final class LootIntegration {

    /** Coffres cibles (exploration verticale / aventure). */
    private static final Set<Identifier> TARGETS = Set.of(
            new Identifier("minecraft", "chests/simple_dungeon"),
            new Identifier("minecraft", "chests/shipwreck_supply"),
            new Identifier("minecraft", "chests/jungle_temple"),
            new Identifier("minecraft", "chests/stronghold_library"),
            new Identifier("minecraft", "chests/ancient_city"),
            new Identifier("minecraft", "chests/end_city_treasure")
    );

    /** Chance qu'un coffre concerne contienne un de ces livres. */
    private static final float CHANCE = 0.10F;

    private LootIntegration() {
    }

    public static void register() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source != LootTableSource.VANILLA || !TARGETS.contains(id)) {
                return;
            }

            LootPool.Builder pool = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .conditionally(RandomChanceLootCondition.builder(CHANCE))
                    // Poids : les 3 normaux moins rares, Auto Deploy (tresor) plus rare.
                    .with(book(ModEnchantments.GLIDE, 2, 6))
                    .with(book(ModEnchantments.SOFT_LANDING, 1, 4))
                    .with(book(ModEnchantments.REINFORCED_CANVAS, 2, 4))
                    .with(book(ModEnchantments.AUTO_DEPLOY, 1, 2));

            tableBuilder.pool(pool);
        });
    }

    /** Un livre enchante portant un enchantement de parachute precis. */
    private static LootPoolEntry.Builder<?> book(Enchantment enchantment, int level, int weight) {
        return ItemEntry.builder(Items.ENCHANTED_BOOK)
                .weight(weight)
                .apply(new SetEnchantmentsLootFunction.Builder()
                        .enchantment(enchantment, ConstantLootNumberProvider.create(level)));
    }
}
