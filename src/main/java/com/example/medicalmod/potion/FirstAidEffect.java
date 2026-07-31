package com.example.medicalmod.potion;

import com.example.medicalmod.injury.InjuryManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Premiers secours : soigne TOUTES les blessures du mod d'un coup.
 * Effet instantane (duree 1 tick) applique par le Serum de premiers secours.
 */
public class FirstAidEffect extends StatusEffect {

    public FirstAidEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x66FF99);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getWorld().isClient) {
            return;
        }
        if (entity instanceof PlayerEntity player) {
            InjuryManager.clear(player); // retire fracture, saignement, infection, commotion
        }
        entity.heal(6.0F);
    }
}
