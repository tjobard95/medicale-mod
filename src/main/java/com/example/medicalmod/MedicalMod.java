package com.example.medicalmod;

import com.example.medicalmod.block.ModBlocks;
import com.example.medicalmod.block.entity.ModBlockEntities;
import com.example.medicalmod.block.entity.PotionMachineBlockEntity;
import com.example.medicalmod.screen.ModScreenHandlers;
import com.example.medicalmod.enchantment.ModEnchantments;
import com.example.medicalmod.injury.InjuryManager;
import com.example.medicalmod.inventory.BackSlotAccess;
import com.example.medicalmod.item.ModItemGroups;
import com.example.medicalmod.item.ModItems;
import com.example.medicalmod.item.ParachuteController;
import com.example.medicalmod.loot.LootIntegration;
import com.example.medicalmod.potion.ModEffects;
import com.example.medicalmod.potion.ModPotions;
import com.example.medicalmod.network.ModNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MedicalMod implements ModInitializer {
    public static final String MOD_ID = "medicalmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // L'ordre est important : les blocs avant les items.
        ModBlocks.registerModBlocks();
        ModBlockEntities.registerBlockEntities();
        ModScreenHandlers.registerScreenHandlers();
        ModItems.registerModItems();
        ModEnchantments.registerModEnchantments();
        // Les effets et potions AVANT le groupe creatif : celui-ci y fait reference.
        ModEffects.registerModEffects();
        ModPotions.registerModPotions();
        ModItemGroups.registerItemGroups();
        PotionMachineBlockEntity.initRecipes();

        registerTickLoop();
        registerInjuryTriggers();
        registerSyncEvents();
        LootIntegration.register();

        LOGGER.info("Medical Mod charge !");
    }

    /** Une seule boucle serveur : parachute + effets de blessure + synchro. */
    private void registerTickLoop() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                ParachuteController.tick(player);
                InjuryManager.tick(player);
                InjuryManager.syncIfDirty(player);
            }
        });
    }

    /** Les degats subis peuvent provoquer une blessure. */
    private void registerInjuryTriggers() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof ServerPlayerEntity player) {
                InjuryManager.onPlayerDamaged(player, source, amount);
            }
            return true; // on ne bloque jamais les degats, on ajoute juste des blessures
        });
    }

    private void registerSyncEvents() {
        // Nouveau joueur : il recoit l'etat de tout le monde.
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ModNetworking.syncAllTo(handler.player);
            ((BackSlotAccess) handler.player).medicalmod$markDirty();
        });

        // Un joueur entre dans le champ de vision d'un autre.
        EntityTrackingEvents.START_TRACKING.register((tracked, player) -> {
            if (tracked instanceof ServerPlayerEntity trackedPlayer) {
                ModNetworking.syncPlayerState(trackedPlayer);
            }
        });

        // Respawn / passage par le portail de l'End : on garde la case du dos.
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            BackSlotAccess from = (BackSlotAccess) oldPlayer;
            BackSlotAccess to = (BackSlotAccess) newPlayer;

            if (alive) {
                // Changement de dimension : on transfere tout.
                to.medicalmod$getBackInventory().setStack(0, from.medicalmod$getBackInventory().getStack(0));
                to.medicalmod$getInjuries().copyFrom(from.medicalmod$getInjuries());
            } else {
                // Apres la mort : le parachute a deja ete drop (ou garde avec keepInventory).
                to.medicalmod$getBackInventory().setStack(0, from.medicalmod$getBackInventory().getStack(0));
                to.medicalmod$getInjuries().clear();
            }
            to.medicalmod$markDirty();
        });
    }
}
