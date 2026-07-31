package com.example.medicalmod.client.render;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

/** Petit sac de parachute porte dans le dos. */
public class ParachutePackModel extends Model {
    private final ModelPart root;

    public ParachutePackModel(ModelPart root) {
        super(RenderLayer::getEntityCutoutNoCull);
        this.root = root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // Sac principal
        root.addChild("pack",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4.0F, 0.5F, 0.0F, 8.0F, 8.0F, 3.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        // Sangle qui passe sur le torse
        root.addChild("strap",
                ModelPartBuilder.create()
                        .uv(0, 12)
                        .cuboid(-4.5F, 1.5F, -2.6F, 9.0F, 3.0F, 6.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 48, 32);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay,
                       float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
