package com.example.medicalmod.client;

import com.example.medicalmod.MedicalMod;
import com.example.medicalmod.block.ModBlocks;
import com.example.medicalmod.client.render.ParachuteFeatureRenderer;
import com.example.medicalmod.client.render.ParachutePackModel;
import com.example.medicalmod.injury.InjuryType;
import com.example.medicalmod.network.ModNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import com.example.medicalmod.screen.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.EnumSet;
import java.util.Set;

public class MedicalModClient implements ClientModInitializer {

    public static final EntityModelLayer PARACHUTE_PACK_LAYER =
            new EntityModelLayer(new Identifier(MedicalMod.MOD_ID, "parachute_pack"), "main");

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COTTON_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ALOE_VERA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHAMOMILE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CALENDULA, RenderLayer.getCutout());

        EntityModelLayerRegistry.registerModelLayer(
                PARACHUTE_PACK_LAYER, ParachutePackModel::getTexturedModelData);

        HandledScreens.register(ModScreenHandlers.POTION_MACHINE, PotionMachineScreen::new);

        registerBackRenderer();
        registerPacketHandler();

        HudRenderCallback.EVENT.register(InjuryHudOverlay::render);
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ClientPlayerState.clear());
    }

    private void registerBackRenderer() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, entityRenderer, registrationHelper, context) -> {
                    if (entityRenderer instanceof PlayerEntityRenderer playerRenderer) {
                        registrationHelper.register(
                                new ParachuteFeatureRenderer<>(playerRenderer, context.getModelLoader()));
                    }
                });
    }

    private void registerPacketHandler() {
        ClientPlayNetworking.registerGlobalReceiver(ModNetworking.SYNC_PLAYER_STATE,
                (client, handler, buf, responseSender) -> {
                    int entityId = buf.readVarInt();
                    ItemStack back = buf.readItemStack();

                    int count = buf.readVarInt();
                    Set<InjuryType> injuries = EnumSet.noneOf(InjuryType.class);
                    for (int i = 0; i < count; i++) {
                        InjuryType type = InjuryType.byId(buf.readString());
                        if (type != null) {
                            injuries.add(type);
                        }
                    }

                    client.execute(() -> ClientPlayerState.update(entityId, back, injuries));
                });
    }
}
