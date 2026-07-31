package com.example.medicalmod.injury;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Les blessures possibles.
 *
 * blocksNaturalRegen = true  -> manger ne soigne plus, il FAUT l'item de soin.
 * curedBy                    -> nom de l'item (sans namespace) qui soigne la blessure.
 */
public enum InjuryType {
    // id            bloque regen  couleur   couleur HUD  item de soin (cle de trad)
    BROKEN_LEG("broken_leg", false, Formatting.GOLD, 0xFFAA00, "item.medicalmod.splint"),
    BLEEDING("bleeding", true, Formatting.RED, 0xFF5555, "item.medicalmod.bandage"),
    INFECTION("infection", true, Formatting.DARK_GREEN, 0x55AA55, "item.medicalmod.medicine"),
    CONCUSSION("concussion", false, Formatting.LIGHT_PURPLE, 0xFF77FF, "item.medicalmod.painkiller");

    private final String id;
    private final boolean blocksNaturalRegen;
    private final Formatting color;
    private final int hudColor;
    private final String cureKey;

    InjuryType(String id, boolean blocksNaturalRegen, Formatting color, int hudColor, String cureKey) {
        this.id = id;
        this.blocksNaturalRegen = blocksNaturalRegen;
        this.color = color;
        this.hudColor = hudColor;
        this.cureKey = cureKey;
    }

    /** Nom de l'item qui soigne cette blessure (ex : Attelle). */
    public Text getCureName() {
        return Text.translatable(this.cureKey);
    }

    /** Couleur RGB utilisee pour le texte du HUD. */
    public int getHudColor() {
        return this.hudColor;
    }

    public String getId() {
        return this.id;
    }

    public boolean blocksNaturalRegen() {
        return this.blocksNaturalRegen;
    }

    public Formatting getColor() {
        return this.color;
    }

    public String getTranslationKey() {
        return "injury.medicalmod." + this.id;
    }

    public Text getDisplayName() {
        return Text.translatable(this.getTranslationKey()).formatted(this.color);
    }

    public static InjuryType byId(String id) {
        for (InjuryType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        return null;
    }
}
