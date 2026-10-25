package com.example.bizarremod.entity;

import com.example.bizarremod.data.JojoData;
import com.example.bizarremod.data.StandType;
import java.util.UUID;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/** Lo stand: un'entita' che segue il proprietario e sparisce quando viene richiamato. */
public class StandEntity extends MobEntity {
    private static final DataParameter<String> STAND_ID =
            EntityDataManager.createKey(StandEntity.class, DataSerializers.STRING);

    private UUID ownerId;

    public StandEntity(EntityType<? extends StandEntity> type, World world) {
        super(type, world);
        this.noClip = true;
        this.setNoGravity(true);
        this.enablePersistence();
    }

    /** Attributi minimi (servono a LivingEntity anche se lo stand non combatte da solo). */
    public static AttributeModifierMap createAttributes() {
        return AttributeModifierMap.createMutableAttribute()
                .createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D)
                .createMutableAttribute(Attributes.ARMOR, 0.0D)
                .createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 0.0D)
                .create();
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(STAND_ID, StandType.STAR_PLATINUM.id);
    }

    public void setOwner(PlayerEntity p) { this.ownerId = p.getUniqueID(); }

    public void setStandType(StandType t) { this.dataManager.set(STAND_ID, t.id); }

    public StandType getStandType() { return StandType.byId(this.dataManager.get(STAND_ID)); }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isRemote) return;

        PlayerEntity owner = ownerId == null ? null : this.world.getPlayerByUuid(ownerId);
        if (owner == null || !owner.isAlive() || !JojoData.get(owner).standActive) {
            this.remove();
            return;
        }

        // Si posiziona dietro e a destra del proprietario
        double yaw = Math.toRadians(owner.rotationYaw);
        double offX = 0.9 * (-Math.cos(yaw) + Math.sin(yaw));
        double offZ = 0.9 * (-Math.sin(yaw) - Math.cos(yaw));
        this.setLocationAndAngles(owner.getPosX() + offX, owner.getPosY() + 0.2, owner.getPosZ() + offZ,
                owner.rotationYaw, owner.rotationPitch);
        this.setRotationYawHead(owner.rotationYawHead);
        this.renderYawOffset = owner.renderYawOffset;
        this.fallDistance = 0;

        if (owner.isSwingInProgress && !this.isSwingInProgress) {
            this.swingArm(Hand.MAIN_HAND);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false; // lo stand non si puo' ferire
    }
}
