package com.example.bizarremod.client;

import com.example.bizarremod.BizarreMod;
import com.example.bizarremod.entity.StandEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;

/** Disegna lo stand con un modello umanoide e una texture diversa per ogni stand. */
public class StandRenderer extends MobRenderer<StandEntity, BipedModel<StandEntity>> {

    public StandRenderer(EntityRendererManager manager) {
        super(manager, new BipedModel<StandEntity>(0.0F), 0.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(StandEntity stand) {
        return BizarreMod.id("textures/entity/stand/" + stand.getStandType().id + ".png");
    }
}
