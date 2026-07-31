package com.example.medicalmod.client;

import com.example.medicalmod.MedicalMod;
import com.example.medicalmod.injury.InjuryType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Set;

/**
 * Retour visuel des blessures :
 *  - icone + nom + soin a utiliser, en haut a gauche (plus besoin de deviner l'item) ;
 *  - voile colore sur les bords, une couleur par blessure (rouge saignement,
 *    vert infection, violet commotion, or jambe cassee).
 */
public final class InjuryHudOverlay {

    private static final int ICON_SIZE = 16;
    private static final int MARGIN = 6;

    private InjuryHudOverlay() {
    }

    public static void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        if (player == null || client.options.hudHidden || player.isSpectator()) {
            return;
        }

        Set<InjuryType> injuries = ClientPlayerState.getInjuries(player.getId());
        if (injuries.isEmpty()) {
            return;
        }

        float time = player.age + tickDelta;

        // --- Voile colore par blessure (le saignement domine, les autres restent discrets) ---
        for (InjuryType injury : injuries) {
            drawVignette(context, time, injury.getHudColor() & 0xFFFFFF, peakAlpha(injury));
        }

        // --- Liste "icone + Blessure -> Soin" ---
        int y = MARGIN;
        for (InjuryType injury : injuries) {
            Identifier icon = new Identifier(MedicalMod.MOD_ID,
                    "textures/gui/injury/" + injury.getId() + ".png");

            RenderSystem.enableBlend();
            context.drawTexture(icon, MARGIN, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
            RenderSystem.disableBlend();

            Text label = injury.getDisplayName().copy()
                    .append(Text.literal("  →  ").formatted(Formatting.GRAY))
                    .append(injury.getCureName().copy().formatted(Formatting.WHITE));
            context.drawTextWithShadow(client.textRenderer, label,
                    MARGIN + ICON_SIZE + 4, y + 4, 0xFFFFFF);

            y += ICON_SIZE + 2;
        }
    }

    /** Intensite max du voile selon la gravite (sur 255). Le saignement est le plus visible. */
    private static int peakAlpha(InjuryType injury) {
        return switch (injury) {
            case BLEEDING -> 90;
            case INFECTION -> 55;
            case CONCUSSION -> 50;
            case BROKEN_LEG -> 40;
        };
    }

    private static void drawVignette(DrawContext context, float time, int rgb, int peakAlpha) {
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        // Pulsation lente (entre ~40% et 100% de peakAlpha).
        float pulse = (float) ((Math.sin(time * 0.08D) + 1.0D) * 0.5D);
        int alpha = (int) (peakAlpha * (0.4F + 0.6F * pulse));
        int color = (alpha << 24) | rgb;
        int transparent = rgb; // meme teinte, alpha 0

        int band = Math.max(24, height / 6);

        RenderSystem.enableBlend();
        context.fillGradient(0, 0, width, band, color, transparent);
        context.fillGradient(0, height - band, width, height, transparent, color);
        context.fillGradient(0, 0, band, height, color, transparent);
        context.fillGradient(width - band, 0, width, height, transparent, color);
        RenderSystem.disableBlend();
    }
}
