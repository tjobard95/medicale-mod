package com.example.medicalmod.injury;

import com.example.medicalmod.enchantment.ModEnchantments;
import com.example.medicalmod.inventory.BackSlotAccess;
import com.example.medicalmod.network.ModNetworking;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.joml.Vector3f;

/**
 * Coeur du systeme de blessures : application, effets par tick et guerison.
 */
public final class InjuryManager {

    // --- Reglages rapides (modifiables sans toucher au reste) ---
    /** Degats de chute a partir desquels une fracture est possible. */
    public static final float FALL_THRESHOLD = 5.0F;
    /** Probabilite de fracture par point de degat de chute au-dela du seuil. */
    public static final float FALL_CHANCE_PER_POINT = 0.14F;
    /** Degats tranchants a partir desquels un saignement est possible. */
    public static final float BLEED_THRESHOLD = 4.0F;
    public static final float BLEED_CHANCE = 0.22F;
    public static final float INFECTION_CHANCE = 0.12F;
    /** Interval entre deux ticks de degats de saignement. */
    public static final int BLEED_INTERVAL = 100; // 5 secondes
    public static final float BLEED_DAMAGE = 1.0F;

    private static final DustParticleEffect BLOOD =
            new DustParticleEffect(new Vector3f(0.62F, 0.03F, 0.03F), 1.0F);

    private InjuryManager() {
    }

    // ---------------------------------------------------------------- lecture

    public static InjuryData get(PlayerEntity player) {
        return ((BackSlotAccess) player).medicalmod$getInjuries();
    }

    public static boolean blocksNaturalRegen(PlayerEntity player) {
        return get(player).blocksNaturalRegen();
    }

    // ---------------------------------------------------------- application

    /** Inflige une blessure et previent le joueur. */
    public static void injure(ServerPlayerEntity player, InjuryType type) {
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        if (get(player).add(type)) {
            ((BackSlotAccess) player).medicalmod$markDirty();
            player.sendMessage(Text.translatable("message.medicalmod.injured", type.getDisplayName(), type.getCureName()), true);
            playInjurySound(player, type);
        }
    }

    /** Son d'apparition, choisi pour etre reconnaissable a l'oreille sans regarder le HUD. */
    private static void playInjurySound(ServerPlayerEntity player, InjuryType type) {
        SoundEvent sound;
        float volume = 1.0F;
        float pitch = 1.0F;
        switch (type) {
            case BROKEN_LEG -> { sound = SoundEvents.BLOCK_BAMBOO_BREAK; pitch = 0.6F; }   // craquement sec
            case BLEEDING -> { sound = SoundEvents.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH; pitch = 0.9F; } // dechirure
            case INFECTION -> { sound = SoundEvents.ENTITY_ZOMBIE_INFECT; pitch = 1.0F; }  // contamination
            case CONCUSSION -> { sound = SoundEvents.BLOCK_BELL_USE; volume = 0.8F; pitch = 1.5F; } // acouphene
            default -> sound = SoundEvents.ENTITY_PLAYER_HURT;
        }
        player.getWorld().playSound(null, player.getBlockPos(), sound, SoundCategory.PLAYERS, volume, pitch);
    }

    /** Soigne une blessure. @return true si le joueur l'avait bien. */
    public static boolean cure(PlayerEntity player, InjuryType type) {
        if (get(player).remove(type)) {
            ((BackSlotAccess) player).medicalmod$markDirty();
            if (player instanceof ServerPlayerEntity sp) {
                sp.sendMessage(Text.translatable("message.medicalmod.cured", type.getDisplayName())
                        .formatted(Formatting.GREEN), true);
            }
            return true;
        }
        return false;
    }

    public static void clear(PlayerEntity player) {
        get(player).clear();
        ((BackSlotAccess) player).medicalmod$markDirty();
    }

    // ------------------------------------------------------- declencheurs

    /**
     * Appele a chaque fois qu'un joueur prend des degats
     * (via ServerLivingEntityEvents.ALLOW_DAMAGE).
     */
    public static void onPlayerDamaged(ServerPlayerEntity player, DamageSource source, float amount) {
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        var random = player.getRandom();

        // --- Chute -> fracture (+ commotion sur grosse chute) ---
        if (source.isOf(DamageTypes.FALL) && amount >= FALL_THRESHOLD) {
            float chance = (amount - FALL_THRESHOLD) * FALL_CHANCE_PER_POINT;
            // Le parachute equipe avec "Atterrissage en douceur" divise le risque.
            int soft = getBackEnchantLevel(player, ModEnchantments.SOFT_LANDING);
            if (soft > 0) {
                chance /= (1.0F + soft);
            }
            if (random.nextFloat() < Math.min(chance, 0.9F)) {
                injure(player, InjuryType.BROKEN_LEG);
            }
            if (amount >= 12.0F && random.nextFloat() < 0.35F) {
                injure(player, InjuryType.CONCUSSION);
            }
            return;
        }

        // --- Explosions / enclumes -> commotion ---
        if (source.isIn(DamageTypeTags.IS_EXPLOSION)
                || source.isOf(DamageTypes.FALLING_ANVIL)
                || source.isOf(DamageTypes.FALLING_BLOCK)) {
            if (random.nextFloat() < 0.4F) {
                injure(player, InjuryType.CONCUSSION);
            }
            return;
        }

        // --- Sources tranchantes / piquantes -> saignement ---
        boolean sharp = source.getAttacker() instanceof LivingEntity
                || source.isOf(DamageTypes.CACTUS)
                || source.isOf(DamageTypes.SWEET_BERRY_BUSH)
                || source.isIn(DamageTypeTags.IS_PROJECTILE);
        if (sharp && amount >= BLEED_THRESHOLD && random.nextFloat() < BLEED_CHANCE) {
            injure(player, InjuryType.BLEEDING);
        }

        // --- Morsures de zombies / mobs -> infection ---
        if (source.getAttacker() instanceof ZombieEntity
                || (source.getAttacker() instanceof HostileEntity && get(player).has(InjuryType.BLEEDING))) {
            if (random.nextFloat() < INFECTION_CHANCE) {
                injure(player, InjuryType.INFECTION);
            }
        }
    }

    // ------------------------------------------------------------- effets

    /** Appele chaque tick serveur pour chaque joueur. */
    public static void tick(ServerPlayerEntity player) {
        InjuryData data = get(player);
        if (data.isEmpty()) {
            return;
        }
        ServerWorld world = player.getServerWorld();

        // Jambe cassee : lenteur forte, sprint et saut coupes.
        if (data.has(InjuryType.BROKEN_LEG)) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS, 40, 1, true, false, false));
            // Le saut est bloque proprement dans LivingEntityJumpMixin.
            if (player.isSprinting()) {
                player.setSprinting(false);
            }
        }

        // Saignement : degats reguliers + particules de sang visibles par tous.
        if (data.has(InjuryType.BLEEDING)) {
            if (player.age % 10 == 0) {
                world.spawnParticles(BLOOD,
                        player.getX(), player.getY() + 0.9D, player.getZ(),
                        3, 0.25D, 0.35D, 0.25D, 0.0D);
            }
            if (player.age % BLEED_INTERVAL == 0 && player.getHealth() > 1.0F) {
                player.damage(player.getDamageSources().generic(), BLEED_DAMAGE);
            }
            // Rappel sonore discret : petite goutte.
            if (player.age % 55 == 0) {
                world.playSound(null, player.getBlockPos(),
                        SoundEvents.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, SoundCategory.PLAYERS, 0.25F, 1.3F);
            }
        }

        // Infection : faiblesse + fatigue, et la faim descend plus vite.
        if (data.has(InjuryType.INFECTION)) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.WEAKNESS, 40, 0, true, false, false));
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.MINING_FATIGUE, 40, 0, true, false, false));
            if (player.age % 40 == 0) {
                player.addExhaustion(0.5F);
            }
            // Rappel sonore discret : gloussement malsain.
            if (player.age % 75 == 0) {
                world.playSound(null, player.getBlockPos(),
                        SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.PLAYERS, 0.2F, 0.7F);
            }
        }

        // Commotion : nausee + vision qui se brouille par a-coups.
        if (data.has(InjuryType.CONCUSSION)) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.NAUSEA, 60, 0, true, false, false));
            if (player.age % 200 == 0) {
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.BLINDNESS, 40, 0, true, false, false));
            }
        }
    }

    // ------------------------------------------------------------- outils

    public static int getBackEnchantLevel(PlayerEntity player, net.minecraft.enchantment.Enchantment enchantment) {
        ItemStack back = ((BackSlotAccess) player).medicalmod$getBackInventory().getStack(0);
        if (back.isEmpty()) {
            return 0;
        }
        return EnchantmentHelper.getLevel(enchantment, back);
    }

    /** Renvoie true si le joueur peut sauter (pas de jambe cassee). */
    public static boolean canJump(PlayerEntity player) {
        return !get(player).has(InjuryType.BROKEN_LEG);
    }

    /** Synchronise les blessures + la case du dos vers les clients concernes. */
    public static void syncIfDirty(ServerPlayerEntity player) {
        BackSlotAccess access = (BackSlotAccess) player;
        if (access.medicalmod$isDirty()) {
            ModNetworking.syncPlayerState(player);
            access.medicalmod$clearDirty();
        }
    }
}
