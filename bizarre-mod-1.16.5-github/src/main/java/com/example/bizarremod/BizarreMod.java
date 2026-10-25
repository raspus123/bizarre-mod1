package com.example.bizarremod;

import com.example.bizarremod.entity.StandEntity;
import com.example.bizarremod.network.ModNetwork;
import com.example.bizarremod.registry.ModEntities;
import com.example.bizarremod.registry.ModItems;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BizarreMod.MOD_ID)
public class BizarreMod {
    public static final String MOD_ID = "bizarremod";

    public BizarreMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITIES.register(modBus);
        ModItems.ITEMS.register(modBus);
        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModNetwork.register();
            GlobalEntityTypeAttributes.put(ModEntities.STAND.get(), StandEntity.createAttributes());
        });
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
