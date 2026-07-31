package com.example.medicalmod.enchantment;

import com.example.medicalmod.item.ParachuteItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * Base des enchantements reserves au parachute.
 *
 * Vanilla decide seul de leur apparition :
 *  - dans la table d'enchantement : tout enchantement dont isAcceptableItem(parachute)
 *    renvoie true est propose. Pas besoin de mixin.
 *  - sur un livre (trouve, peche, marchand) : vanilla accepte TOUT enchantement
 *    non-tresor sur un livre, donc les notres apparaissent deja.
 *
 * Le flag "treasure" sert a en reserver certains a la trouvaille : un enchantement
 * tresor n'apparait JAMAIS a la table, uniquement sous forme de livre trouve/achete.
 */
public class ParachuteEnchantment extends Enchantment {
    private final int maxLevel;
    private final boolean treasure;

    protected ParachuteEnchantment(Rarity rarity, int maxLevel, boolean treasure) {
        super(rarity, EnchantmentTarget.BREAKABLE, new EquipmentSlot[]{EquipmentSlot.CHEST});
        this.maxLevel = maxLevel;
        this.treasure = treasure;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    public int getMinPower(int level) {
        return 8 + (level - 1) * 10;
    }

    @Override
    public int getMaxPower(int level) {
        return this.getMinPower(level) + 25;
    }

    @Override
    public boolean isTreasure() {
        return this.treasure;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ParachuteItem;
    }
}
