package com.example.medicalmod.client;

import com.example.medicalmod.MedicalMod;
import com.example.medicalmod.screen.PotionMachineScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Interface du Distillateur nutritif.
 * Texture sur mesure : 2 entrees a gauche, fleche de progression, 1 sortie a droite.
 */
public class PotionMachineScreen extends HandledScreen<PotionMachineScreenHandler> {

    private static final Identifier TEXTURE =
            new Identifier(MedicalMod.MOD_ID, "textures/gui/potion_machine.png");

    /** Fleche : position dans le GUI et taille (doit coller a la texture). */
    private static final int ARROW_X = 70;
    private static final int ARROW_Y = 37;
    private static final int ARROW_WIDTH = 38;
    private static final int ARROW_HEIGHT = 12;
    /** Position de la fleche "pleine" dans le fichier texture. */
    private static final int ARROW_TEXTURE_U = 176;
    private static final int ARROW_TEXTURE_V = 0;

    public PotionMachineScreen(PotionMachineScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 166;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        // La fleche se remplit de gauche a droite au fil de la fabrication.
        int progress = (int) (ARROW_WIDTH * this.handler.getProgressRatio());
        if (progress > 0) {
            context.drawTexture(TEXTURE, x + ARROW_X, y + ARROW_Y,
                    ARROW_TEXTURE_U, ARROW_TEXTURE_V, progress, ARROW_HEIGHT);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
