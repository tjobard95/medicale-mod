package com.example.medicalmod.mixin;

import com.example.medicalmod.injury.InjuryData;
import com.example.medicalmod.inventory.BackSlotAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Ajoute au joueur : la case du dos (parachute) et l'etat des blessures,
 * avec sauvegarde NBT et gestion de la mort.
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements BackSlotAccess {

    @Unique
    private SimpleInventory medicalmod$backInventory;

    @Unique
    private InjuryData medicalmod$injuries;

    @Unique
    private boolean medicalmod$dirty = true;

    @Override
    public SimpleInventory medicalmod$getBackInventory() {
        if (this.medicalmod$backInventory == null) {
            this.medicalmod$backInventory = new SimpleInventory(1);
        }
        return this.medicalmod$backInventory;
    }

    @Override
    public InjuryData medicalmod$getInjuries() {
        if (this.medicalmod$injuries == null) {
            this.medicalmod$injuries = new InjuryData();
        }
        return this.medicalmod$injuries;
    }

    @Override
    public void medicalmod$markDirty() {
        this.medicalmod$dirty = true;
    }

    @Override
    public boolean medicalmod$isDirty() {
        return this.medicalmod$dirty;
    }

    @Override
    public void medicalmod$clearDirty() {
        this.medicalmod$dirty = false;
    }

    // ---------------------------------------------------------- sauvegarde

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void medicalmod$write(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound data = new NbtCompound();
        ItemStack back = this.medicalmod$getBackInventory().getStack(0);
        if (!back.isEmpty()) {
            data.put("Back", back.writeNbt(new NbtCompound()));
        }
        data.put("Injuries", this.medicalmod$getInjuries().writeNbt());
        nbt.put("MedicalModData", data);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void medicalmod$read(NbtCompound nbt, CallbackInfo ci) {
        if (!nbt.contains("MedicalModData", 10)) { // 10 = NbtCompound
            return;
        }
        NbtCompound data = nbt.getCompound("MedicalModData");
        this.medicalmod$getBackInventory().setStack(0,
                data.contains("Back", 10) ? ItemStack.fromNbt(data.getCompound("Back")) : ItemStack.EMPTY);
        if (data.contains("Injuries", 10)) {
            this.medicalmod$getInjuries().readNbt(data.getCompound("Injuries"));
        }
        this.medicalmod$markDirty();
    }

    // --------------------------------------------------------------- mort

    @Inject(method = "dropInventory", at = @At("HEAD"))
    private void medicalmod$dropBackSlot(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;

        // On repart toujours indemne apres une mort.
        this.medicalmod$getInjuries().clear();

        if (self.getWorld().isClient
                || self.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            this.medicalmod$markDirty();
            return;
        }

        ItemStack back = this.medicalmod$getBackInventory().getStack(0);
        if (!back.isEmpty()) {
            self.dropItem(back, true, false);
            this.medicalmod$getBackInventory().setStack(0, ItemStack.EMPTY);
        }
        this.medicalmod$markDirty();
    }
}
