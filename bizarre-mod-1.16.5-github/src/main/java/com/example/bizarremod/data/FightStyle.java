package com.example.bizarremod.data;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public enum FightStyle {
    NONE("none"), HAMON("hamon"), SPIN("spin"), BOXING("boxing");

    public final String id;

    FightStyle(String id) { this.id = id; }

    public ITextComponent displayName() { return new TranslationTextComponent("style.bizarremod." + id); }

    public static FightStyle byId(String id) {
        for (FightStyle s : values()) if (s.id.equals(id)) return s;
        return NONE;
    }
}
