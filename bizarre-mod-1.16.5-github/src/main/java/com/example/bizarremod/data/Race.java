package com.example.bizarremod.data;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public enum Race {
    HUMAN("human"), VAMPIRE("vampire"), PILLAR_MAN("pillar_man");

    public final String id;

    Race(String id) { this.id = id; }

    public ITextComponent displayName() { return new TranslationTextComponent("race.bizarremod." + id); }

    public static Race byId(String id) {
        for (Race r : values()) if (r.id.equals(id)) return r;
        return HUMAN;
    }
}
