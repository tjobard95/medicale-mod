package com.example.medicalmod.mixin;

import com.example.medicalmod.inventory.BackSlot;
import com.example.medicalmod.inventory.BackSlotAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Ajoute la case "dos" (index 46) juste au-dessus de la main secondaire.
 * Position ecran : x=77, y=44 (la main secondaire est en x=77, y=62).
 */
@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends ScreenHandler {

    protected PlayerScreenHandlerMixin(ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void medicalmod$addBackSlot(PlayerInventory inventory, boolean onServer,
                                       PlayerEntity owner, CallbackInfo ci) {
        this.addSlot(new BackSlot(((BackSlotAccess) owner).medicalmod$getBackInventory(), 0, 77, 44));
    }
}
