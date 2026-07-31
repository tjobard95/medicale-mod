package com.example.medicalmod.inventory;

import com.example.medicalmod.injury.InjuryData;
import net.minecraft.inventory.SimpleInventory;

/**
 * Interface "duck-typing" implantee sur PlayerEntity par PlayerEntityMixin.
 * Donne acces a la case du dos (parachute) et aux blessures du joueur.
 */
public interface BackSlotAccess {
    SimpleInventory medicalmod$getBackInventory();

    InjuryData medicalmod$getInjuries();

    /** Marque les donnees comme modifiees -> resynchronisation au prochain tick. */
    void medicalmod$markDirty();

    boolean medicalmod$isDirty();

    void medicalmod$clearDirty();
}
