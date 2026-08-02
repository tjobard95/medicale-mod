package com.example.medicalmod.injury;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Etat des blessures d'un joueur.
 * Une blessure reste active jusqu'a ce qu'on utilise le bon item de soin :
 * il n'y a pas de guerison automatique.
 */
public class InjuryData {
    private final Set<InjuryType> active = EnumSet.noneOf(InjuryType.class);

    /** Tick monde de la derniere blessure recue, pour le delai de repit. */
    private long lastInjuryTick = Long.MIN_VALUE;

    public long getLastInjuryTick() {
        return this.lastInjuryTick;
    }

    public void setLastInjuryTick(long tick) {
        this.lastInjuryTick = tick;
    }

    public int count() {
        return this.active.size();
    }

    public boolean has(InjuryType type) {
        return this.active.contains(type);
    }

    public boolean isEmpty() {
        return this.active.isEmpty();
    }

    /** @return true si la blessure n'etait pas deja presente. */
    public boolean add(InjuryType type) {
        return this.active.add(type);
    }

    /** @return true si la blessure etait presente et a ete soignee. */
    public boolean remove(InjuryType type) {
        return this.active.remove(type);
    }

    public void clear() {
        this.active.clear();
    }

    public Set<InjuryType> getActive() {
        return Collections.unmodifiableSet(this.active);
    }

    /** Au moins une blessure empeche la regeneration naturelle ? */
    public boolean blocksNaturalRegen() {
        for (InjuryType type : this.active) {
            if (type.blocksNaturalRegen()) {
                return true;
            }
        }
        return false;
    }

    public void copyFrom(InjuryData other) {
        this.active.clear();
        this.active.addAll(other.active);
    }

    // ---------- Serialisation ----------

    public NbtCompound writeNbt() {
        NbtCompound nbt = new NbtCompound();
        NbtList list = new NbtList();
        for (InjuryType type : this.active) {
            list.add(NbtString.of(type.getId()));
        }
        nbt.put("Active", list);
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        this.active.clear();
        NbtList list = nbt.getList("Active", 8); // 8 = NbtString
        for (int i = 0; i < list.size(); i++) {
            InjuryType type = InjuryType.byId(list.getString(i));
            if (type != null) {
                this.active.add(type);
            }
        }
    }
}
