package com.example.bizarremod.network;

import com.example.bizarremod.BizarreMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(BizarreMod.MOD_ID, "main"),
            () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    public static void register() {
        CHANNEL.registerMessage(0, ActionPacket.class, ActionPacket::encode, ActionPacket::decode, ActionPacket::handle);
    }
}
