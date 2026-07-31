package com.example.medicalmod.mixin.client;

import com.example.medicalmod.MedicalMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Dessine le fond de la case "dos" et son icone quand elle est vide. */
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {

    private static final Identifier VANILLA_INVENTORY =
            new Identifier("minecraft", "textures/gui/container/inventory.png");
    private static final Identifier BACK_SLOT_ICON =
            new Identifier(MedicalMod.MOD_ID, "textures/gui/back_slot.png");

    public InventoryScreenMixin(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void medicalmod$drawBackSlotBackground(DrawContext context, float delta,
                                                  int mouseX, int mouseY, CallbackInfo ci) {
        // Fond de case recupere sur la case de main secondaire du GUI vanilla.
        context.drawTexture(VANILLA_INVENTORY, this.x + 76, this.y + 43, 76, 61, 18, 18);

        // Icone "parachute" quand la case est vide.
        if (this.handler.slots.size() > 46 && !this.handler.slots.get(46).hasStack()) {
            context.drawTexture(BACK_SLOT_ICON, this.x + 77, this.y + 44, 0, 0, 16, 16, 16, 16);
        }
    }
}
