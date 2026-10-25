package com.example.bizarremod.client;

import com.example.bizarremod.BizarreMod;
import com.example.bizarremod.registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BizarreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent e) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.STAND.get(), StandRenderer::new);
        ClientRegistry.registerKeyBinding(ClientKeys.SUMMON);
        ClientRegistry.registerKeyBinding(ClientKeys.CYCLE);
        ClientRegistry.registerKeyBinding(ClientKeys.USE);
        ClientRegistry.registerKeyBinding(ClientKeys.STYLE);
        ClientRegistry.registerKeyBinding(ClientKeys.RACE);
    }
}
