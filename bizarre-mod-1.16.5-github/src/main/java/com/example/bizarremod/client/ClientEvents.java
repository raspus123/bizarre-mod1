package com.example.bizarremod.client;

import com.example.bizarremod.BizarreMod;
import com.example.bizarremod.logic.Abilities;
import com.example.bizarremod.network.ActionPacket;
import com.example.bizarremod.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BizarreMod.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.currentScreen != null) return;

        poll(ClientKeys.SUMMON, Abilities.SUMMON);
        poll(ClientKeys.CYCLE, Abilities.CYCLE);
        poll(ClientKeys.USE, Abilities.USE);
        poll(ClientKeys.STYLE, Abilities.STYLE);
        poll(ClientKeys.RACE, Abilities.RACE);
    }

    private static void poll(KeyBinding key, int action) {
        while (key.isPressed()) {
            ModNetwork.CHANNEL.sendToServer(new ActionPacket(action));
        }
    }
}
