package com.example.medicalmod.mixin;

import com.example.medicalmod.injury.InjuryManager;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Coeur de la demande : "meme si on mange on ne se regenere pas".
 * On intercepte les deux appels a heal() de la regeneration par la nourriture.
 * Les items de soin, les potions et les golden apples continuent de marcher.
 */
@Mixin(HungerManager.class)
public class HungerManagerMixin {

    @Redirect(
            method = "update",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;heal(F)V")
    )
    private void medicalmod$blockNaturalRegen(PlayerEntity player, float amount) {
        if (InjuryManager.blocksNaturalRegen(player)) {
            return; // saignement ou infection -> la nourriture ne soigne plus
        }
        player.heal(amount);
    }
}
