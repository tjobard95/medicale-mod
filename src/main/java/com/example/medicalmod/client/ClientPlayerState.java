package com.example.medicalmod.client;

import com.example.medicalmod.injury.InjuryType;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Cache client : ce que le serveur nous a dit sur chaque joueur visible. */
public final class ClientPlayerState {

    private static final Map<Integer, ItemStack> BACK_STACKS = new HashMap<>();
    private static final Map<Integer, Set<InjuryType>> INJURIES = new HashMap<>();

    private ClientPlayerState() {
    }

    public static void update(int entityId, ItemStack back, Set<InjuryType> injuries) {
        if (back.isEmpty()) {
            BACK_STACKS.remove(entityId);
        } else {
            BACK_STACKS.put(entityId, back);
        }
        if (injuries.isEmpty()) {
            INJURIES.remove(entityId);
        } else {
            INJURIES.put(entityId, EnumSet.copyOf(injuries));
        }
    }

    public static ItemStack getBackStack(int entityId) {
        return BACK_STACKS.getOrDefault(entityId, ItemStack.EMPTY);
    }

    public static Set<InjuryType> getInjuries(int entityId) {
        return INJURIES.getOrDefault(entityId, Collections.emptySet());
    }

    public static void clear() {
        BACK_STACKS.clear();
        INJURIES.clear();
    }
}
