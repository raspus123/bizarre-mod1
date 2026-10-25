package com.example.bizarremod.logic;

import com.example.bizarremod.BizarreMod;
import com.example.bizarremod.command.JojoCommand;
import com.example.bizarremod.data.FightStyle;
import com.example.bizarremod.data.JojoData;
import com.example.bizarremod.data.Race;
import com.example.bizarremod.data.StandType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BizarreMod.MOD_ID)
public class GameEvents {

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.getPlayer() instanceof ServerPlayerEntity) {
            JojoData.get(e.getPlayer()).standActive = false;
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent e) {
        if (e.getPlayer() instanceof ServerPlayerEntity) {
            JojoData.saveAndForget(e.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onCommands(RegisterCommandsEvent e) {
        JojoCommand.register(e.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.END || e.side != LogicalSide.SERVER) return;
        if (!(e.player instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity p = (ServerPlayerEntity) e.player;
        JojoData d = JojoData.get(p);

        World level = p.world;
        boolean sun = level.isDaytime() && level.canSeeSky(p.getPosition().up()) && !p.isInWaterRainOrBubble();

        // rigenerazione energia (l'Hamon ricarica di piu' sotto il sole)
        float regen = 0.25f;
        if (d.style == FightStyle.HAMON && sun) regen += 0.15f;
        d.energy = Math.min(100f, d.energy + regen);

        if (p.ticksExisted % 20 != 0) return;

        // ---- salvataggio periodico nei dati del giocatore (ogni 10 secondi) ----
        if (p.ticksExisted % 200 == 0) JojoData.save(p);

        // ---- passive delle razze (ogni secondo) ----
        switch (d.race) {
            case VAMPIRE:
                if (sun && p.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty() && !p.isImmuneToFire()) {
                    p.setFire(5);                          // il sole brucia i vampiri
                } else if (!sun) {
                    eff(p, Effects.STRENGTH, 60, 0);
                    eff(p, Effects.SPEED, 60, 0);
                    eff(p, Effects.NIGHT_VISION, 320, 0);
                }
                break;
            case PILLAR_MAN:
                eff(p, Effects.STRENGTH, 60, 1);
                eff(p, Effects.RESISTANCE, 60, 0);
                eff(p, Effects.JUMP_BOOST, 60, 1);
                if (sun) {                                 // debole alla luce del sole
                    eff(p, Effects.WEAKNESS, 60, 0);
                    eff(p, Effects.SLOWNESS, 60, 0);
                }
                break;
            default:
                break;
        }

        // ---- passiva dello stile ----
        if (d.style == FightStyle.BOXING) eff(p, Effects.SPEED, 60, 0);

        // ---- barra di stato ogni 3 secondi ----
        if (p.ticksExisted % 60 == 0
                && (d.race != Race.HUMAN || d.style != FightStyle.NONE || d.stand != StandType.NONE)) {
            p.sendStatusMessage(new TranslationTextComponent("message.bizarremod.status",
                    (int) d.energy, d.race.displayName(), d.style.displayName(), d.stand.displayName(),
                    Abilities.moveName(d.stand, d.selectedMove)), true);
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent e) {
        // bonus dei pugni dello stand (solo attacco normale del giocatore)
        if ("player".equals(e.getSource().getDamageType())
                && e.getSource().getTrueSource() instanceof ServerPlayerEntity) {
            ServerPlayerEntity attacker = (ServerPlayerEntity) e.getSource().getTrueSource();
            JojoData d = JojoData.get(attacker);
            if (d.standActive && d.stand != StandType.NONE) {
                e.setAmount(e.getAmount() * d.stand.damageMultiplier);
            }
        }
        // lo stand protegge il proprietario (-25% danni)
        if (e.getEntityLiving() instanceof ServerPlayerEntity) {
            JojoData d = JojoData.get((ServerPlayerEntity) e.getEntityLiving());
            if (d.standActive) e.setAmount(e.getAmount() * 0.75f);
        }
    }

    @SubscribeEvent
    public static void onDamageDone(LivingDamageEvent e) {
        // i vampiri recuperano il 20% del danno inflitto
        if (e.getSource().getTrueSource() instanceof ServerPlayerEntity) {
            ServerPlayerEntity attacker = (ServerPlayerEntity) e.getSource().getTrueSource();
            if (JojoData.get(attacker).race == Race.VAMPIRE) {
                attacker.heal(e.getAmount() * 0.2f);
            }
        }
    }

    private static void eff(ServerPlayerEntity p, Effect effect, int duration, int amplifier) {
        p.addPotionEffect(new EffectInstance(effect, duration, amplifier, true, false, true));
    }
}
