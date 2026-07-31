package com.example.medicalmod.mixin;

import com.example.medicalmod.injury.InjuryManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Jambe cassee = plus de saut tant qu'on n'a pas pose d'attelle. */
@Mixin(LivingEntity.class)
public class LivingEntityJumpMixin {

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    private void medicalmod$blockJumpWhenLegBroken(CallbackInfo ci) {
        if ((Object) this instanceof PlayerEntity player) {
            if (!player.isCreative() && !player.isSpectator() && !InjuryManager.canJump(player)) {
                ci.cancel();
            }
        }
    }
}
