package com.example.medicalmod.network;

import com.example.medicalmod.MedicalMod;
import com.example.medicalmod.injury.InjuryType;
import com.example.medicalmod.inventory.BackSlotAccess;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Set;

public final class ModNetworking {

    /** Etat d'un joueur : parachute porte dans le dos + blessures actives. */
    public static final Identifier SYNC_PLAYER_STATE =
            new Identifier(MedicalMod.MOD_ID, "sync_player_state");

    private ModNetworking() {
    }

    /**
     * Envoie l'etat du joueur a lui-meme et a tous ceux qui le voient
     * (necessaire pour afficher le parachute sur son dos).
     */
    public static void syncPlayerState(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, SYNC_PLAYER_STATE, buildPacket(player));
        for (ServerPlayerEntity viewer : PlayerLookup.tracking(player)) {
            if (viewer != player) {
                ServerPlayNetworking.send(viewer, SYNC_PLAYER_STATE, buildPacket(player));
            }
        }
    }

    /** Renvoie l'etat de tous les joueurs connectes a un joueur qui vient d'arriver. */
    public static void syncAllTo(ServerPlayerEntity receiver) {
        for (ServerPlayerEntity other : receiver.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(receiver, SYNC_PLAYER_STATE, buildPacket(other));
        }
    }

    /** Un buffer neuf a chaque envoi : un PacketByteBuf ne se reutilise pas. */
    private static PacketByteBuf buildPacket(ServerPlayerEntity player) {
        BackSlotAccess access = (BackSlotAccess) player;
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeVarInt(player.getId());
        buf.writeItemStack(access.medicalmod$getBackInventory().getStack(0));

        Set<InjuryType> active = access.medicalmod$getInjuries().getActive();
        buf.writeVarInt(active.size());
        for (InjuryType type : active) {
            buf.writeString(type.getId());
        }
        return buf;
    }
}
