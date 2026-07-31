package com.example.medicalmod.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/**
 * Adrenaline : les bonus (Force + Rapidite + Celerite) sont fournis par la potion
 * elle-meme. Ce marqueur ne sert qu'a declencher le CONTRECOUP a la fin :
 * quand l'effet se termine, on applique fatigue + lenteur + faiblesse.
 */
public class AdrenalineEffect extends StatusEffect {

    private static final int CRASH_DURATION = 300; // 15 s de contrecoup

    public AdrenalineEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xFF3B30);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true; // verifie chaque tick pour attraper le dernier
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getWorld().isClient) {
            return;
        }
        StatusEffectInstance self = entity.getStatusEffect(this);
        if (self != null && self.getDuration() <= 1) {
            // Dernier tick : le corps paie la note.
            entity.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.MINING_FATIGUE, CRASH_DURATION, amplifier, false, true, true));
            entity.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS, CRASH_DURATION, 0, false, true, true));
            entity.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.WEAKNESS, CRASH_DURATION, 0, false, true, true));
        }
    }
}
