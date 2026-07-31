package com.example.medicalmod.client;

import com.example.medicalmod.MedicalMod;
import com.example.medicalmod.screen.PotionMachineScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/** Interface du Distillateur nutritif (fond du coffre vanilla + barre de progression). */
public class PotionMachineScreen extends HandledScreen<PotionMachineScreenHandler> {

    private static final Identifier TEXTURE =
            new Identifier("minecraft", "textures/gui/container/generic_54.png");

    public PotionMachineScreen(PotionMachineScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 166;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, 3 * 18 + 17);
        context.drawTexture(TEXTURE, x, y + 3 * 18 + 17, 0, 126, this.backgroundWidth, 96);

        // Barre de progression : rectangle qui se remplit entre les entrees et la sortie.
        float ratio = this.handler.getProgressRatio();
        int width = (int) (48 * ratio);
        if (width > 0) {
            context.fill(x + 64, y + 38, x + 64 + width, y + 44, 0xFFE2A826);
        }
        context.drawBorder(x + 64, y + 38, 48, 6, 0xFF373737);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
