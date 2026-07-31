package com.example.medicalmod.client.render;

import com.example.medicalmod.MedicalMod;
import com.example.medicalmod.client.ClientPlayerState;
import com.example.medicalmod.client.MedicalModClient;
import com.example.medicalmod.item.ParachuteItem;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/** Affiche le parachute sur le dos du joueur, par-dessus l'armure. */
public class ParachuteFeatureRenderer<T extends PlayerEntity, M extends PlayerEntityModel<T>>
        extends FeatureRenderer<T, M> {

    private static final Identifier TEXTURE =
            new Identifier(MedicalMod.MOD_ID, "textures/entity/parachute_pack.png");

    private final ParachutePackModel model;

    public ParachuteFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
        super(context);
        this.model = new ParachutePackModel(loader.getModelPart(MedicalModClient.PARACHUTE_PACK_LAYER));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       T entity, float limbAngle, float limbDistance, float tickDelta,
                       float animationProgress, float headYaw, float headPitch) {

        ItemStack stack = ClientPlayerState.getBackStack(entity.getId());
        if (!(stack.getItem() instanceof ParachuteItem)) {
            return;
        }
        if (entity.isInvisible()) {
            return;
        }

        matrices.push();
        // On suit les rotations du torse pour que le sac reste colle au dos.
        this.getContextModel().body.rotate(matrices);
        matrices.translate(0.0D, 0.0D, 0.135D);

        VertexConsumer consumer = ItemRenderer.getArmorGlintConsumer(
                vertexConsumers, RenderLayer.getEntityCutoutNoCull(TEXTURE), false, stack.hasGlint());

        this.model.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV,
                1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
    }
}
