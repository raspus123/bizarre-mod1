package com.example.bizarremod.network;

import com.example.bizarremod.logic.Abilities;
import java.util.function.Supplier;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/** Pacchetto client -> server: "il giocatore ha premuto il tasto N". */
public class ActionPacket {
    private final int action;

    public ActionPacket(int action) { this.action = action; }

    public static void encode(ActionPacket msg, PacketBuffer buf) { buf.writeVarInt(msg.action); }

    public static ActionPacket decode(PacketBuffer buf) { return new ActionPacket(buf.readVarInt()); }

    public static void handle(ActionPacket msg, Supplier<NetworkEvent.Context> ctx) {
        final NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();
            if (sender != null) Abilities.perform(sender, msg.action);
        });
        context.setPacketHandled(true);
    }
}
