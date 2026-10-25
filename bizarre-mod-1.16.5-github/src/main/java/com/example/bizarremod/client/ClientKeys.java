package com.example.bizarremod.client;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class ClientKeys {
    private static final String CATEGORY = "key.categories.bizarremod";

    public static final KeyBinding SUMMON = new KeyBinding("key.bizarremod.summon", GLFW.GLFW_KEY_R, CATEGORY);
    public static final KeyBinding CYCLE  = new KeyBinding("key.bizarremod.cycle",  GLFW.GLFW_KEY_G, CATEGORY);
    public static final KeyBinding USE    = new KeyBinding("key.bizarremod.use",    GLFW.GLFW_KEY_V, CATEGORY);
    public static final KeyBinding STYLE  = new KeyBinding("key.bizarremod.style",  GLFW.GLFW_KEY_C, CATEGORY);
    public static final KeyBinding RACE   = new KeyBinding("key.bizarremod.race",   GLFW.GLFW_KEY_X, CATEGORY);
}
