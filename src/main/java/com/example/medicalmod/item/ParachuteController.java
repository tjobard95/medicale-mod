package com.example.medicalmod.item;

import com.example.medicalmod.enchantment.ModEnchantments;
import com.example.medicalmod.inventory.BackSlotAccess;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

/**
 * Physique du parachute. Il est lu depuis la case du dos, plus depuis le plastron,
 * ce qui permet de porter une armure complete ET le parachute.
 */
public final class ParachuteController {

    /** Vitesse de descente de base une fois deploye. */
    private static final double BASE_FALL_SPEED = -0.40D;
    /** Glisse horizontale de base. */
    private static final double BASE_GLIDE = 1.10D;
    /** Un point de durabilite consomme toutes les N secondes de vol. */
    private static final int DURABILITY_INTERVAL = 20;

    private ParachuteController() {
    }

    public static void tick(ServerPlayerEntity player) {
        BackSlotAccess access = (BackSlotAccess) player;
        ItemStack parachute = access.medicalmod$getBackInventory().getStack(0);

        if (!(parachute.getItem() instanceof ParachuteItem)) {
            return;
        }

        boolean falling = !player.isOnGround() && player.getVelocity().y < -0.4D;
        if (!falling) {
            return;
        }

        int autoDeploy = EnchantmentHelper.getLevel(ModEnchantments.AUTO_DEPLOY, parachute);
        boolean deployed = player.isSneaking() || autoDeploy > 0;
        if (!deployed) {
            return;
        }

        int glide = EnchantmentHelper.getLevel(ModEnchantments.GLIDE, parachute);
        int softLanding = EnchantmentHelper.getLevel(ModEnchantments.SOFT_LANDING, parachute);
        int reinforced = EnchantmentHelper.getLevel(ModEnchantments.REINFORCED_CANVAS, parachute);

        // Planeur : plus de glisse et descente plus lente.
        double glideFactor = BASE_GLIDE + (glide * 0.09D);
        double fallSpeed = BASE_FALL_SPEED + (glide * 0.045D); // ex : Planeur III -> -0.265

        Vec3d v = player.getVelocity();
        player.setVelocity(v.x * glideFactor, fallSpeed, v.z * glideFactor);
        player.velocityModified = true;

        // Sans "Atterrissage en douceur" il reste un petit reliquat de chute :
        // assez pour rester dangereux si on ouvre trop tard.
        if (softLanding > 0) {
            player.fallDistance = 0.0F;
        } else {
            player.fallDistance = Math.min(player.fallDistance, 3.0F);
        }

        // Bruit de toile + usure.
        if (player.age % 20 == 0) {
            player.getWorld().playSound(null, player.getBlockPos(),
                    SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS, 0.25F, 1.6F);
        }

        if (player.age % (DURABILITY_INTERVAL * (1 + reinforced)) == 0) {
            damageParachute(player, parachute);
        }
    }

    private static void damageParachute(ServerPlayerEntity player, ItemStack parachute) {
        parachute.damage(1, player, p -> {
            p.sendEquipmentBreakStatus(EquipmentSlot.CHEST);
            p.getWorld().playSound(null, p.getBlockPos(),
                    SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
        });
        BackSlotAccess access = (BackSlotAccess) player;
        if (parachute.isEmpty() || parachute.getDamage() >= parachute.getMaxDamage()) {
            access.medicalmod$getBackInventory().setStack(0, ItemStack.EMPTY);
        }
        access.medicalmod$markDirty();
    }
}
