package com.example.bizarremod.data;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public enum StandType {
    //            id                 moltiplicatore danno dei pugni
    NONE("none", 1.0f),
    STAR_PLATINUM("star_platinum", 1.8f),
    THE_WORLD("the_world", 2.0f),
    CRAZY_DIAMOND("crazy_diamond", 1.6f),
    KILLER_QUEEN("killer_queen", 1.3f),
    GOLD_EXPERIENCE("gold_experience", 1.4f),
    SILVER_CHARIOT("silver_chariot", 1.7f),
    MAGICIANS_RED("magicians_red", 1.5f),
    HIEROPHANT_GREEN("hierophant_green", 1.3f),
    KING_CRIMSON("king_crimson", 1.6f),
    STICKY_FINGERS("sticky_fingers", 1.5f),
    STONE_FREE("stone_free", 1.4f);

    public final String id;
    public final float damageMultiplier;

    StandType(String id, float damageMultiplier) {
        this.id = id;
        this.damageMultiplier = damageMultiplier;
    }

    public ITextComponent displayName() { return new TranslationTextComponent("stand.bizarremod." + id); }

    public static StandType byId(String id) {
        for (StandType s : values()) if (s.id.equals(id)) return s;
        return NONE;
    }
}
