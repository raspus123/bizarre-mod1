package com.example.bizarremod.registry;

import com.example.bizarremod.BizarreMod;
import com.example.bizarremod.data.FightStyle;
import com.example.bizarremod.data.JojoData;
import com.example.bizarremod.data.Race;
import com.example.bizarremod.data.StandType;
import com.example.bizarremod.item.ActionItem;
import java.util.function.Predicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BizarreMod.MOD_ID);

    /** Scheda del creativo "Bizarre Mod". */
    public static final ItemGroup TAB = new ItemGroup("bizarremod") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(STAND_ARROW.get());
        }
    };

    public static final RegistryObject<Item> STAND_ARROW = ITEMS.register("stand_arrow",
            () -> new ActionItem(new Item.Properties().group(TAB).maxStackSize(16), ModItems::giveRandomStand));
    public static final RegistryObject<Item> STONE_MASK = ITEMS.register("stone_mask",
            () -> new ActionItem(new Item.Properties().group(TAB).maxStackSize(1), ModItems::stoneMask));
    public static final RegistryObject<Item> RED_STONE_OF_AJA = ITEMS.register("red_stone_of_aja",
            () -> new ActionItem(new Item.Properties().group(TAB).maxStackSize(1), ModItems::redStone));
    public static final RegistryObject<Item> HUMAN_SERUM = ITEMS.register("human_serum",
            () -> new ActionItem(new Item.Properties().group(TAB).maxStackSize(16), ModItems::serum));
    public static final RegistryObject<Item> HAMON_SCROLL = ITEMS.register("hamon_scroll",
            () -> new ActionItem(new Item.Properties().group(TAB).maxStackSize(1), learn(FightStyle.HAMON)));
    public static final RegistryObject<Item> SPIN_SCROLL = ITEMS.register("spin_scroll",
            () -> new ActionItem(new Item.Properties().group(TAB).maxStackSize(1), learn(FightStyle.SPIN)));
    public static final RegistryObject<Item> BOXING_MANUAL = ITEMS.register("boxing_manual",
            () -> new ActionItem(new Item.Properties().group(TAB).maxStackSize(1), learn(FightStyle.BOXING)));

    // ---- azioni degli oggetti ----

    private static boolean giveRandomStand(ServerPlayerEntity p) {
        StandType[] all = StandType.values();
        StandType s = all[1 + p.getRNG().nextInt(all.length - 1)]; // salta NONE
        JojoData d = JojoData.get(p);
        d.stand = s;
        d.standActive = false;
        d.selectedMove = 0;
        p.sendStatusMessage(new TranslationTextComponent("message.bizarremod.stand_gained", s.displayName()), false);
        return true;
    }

    private static Predicate<ServerPlayerEntity> learn(final FightStyle style) {
        return p -> {
            JojoData.get(p).style = style;
            p.sendStatusMessage(new TranslationTextComponent("message.bizarremod.style_learned", style.displayName()), false);
            return true;
        };
    }

    private static boolean stoneMask(ServerPlayerEntity p) {
        JojoData d = JojoData.get(p);
        if (d.race != Race.HUMAN) {
            p.sendStatusMessage(new TranslationTextComponent("message.bizarremod.cannot_use"), true);
            return false;
        }
        d.race = Race.VAMPIRE;
        p.sendStatusMessage(new TranslationTextComponent("message.bizarremod.race_changed", d.race.displayName()), false);
        return true;
    }

    private static boolean redStone(ServerPlayerEntity p) {
        JojoData d = JojoData.get(p);
        if (d.race != Race.VAMPIRE) {
            p.sendStatusMessage(new TranslationTextComponent("message.bizarremod.cannot_use"), true);
            return false;
        }
        d.race = Race.PILLAR_MAN;
        p.sendStatusMessage(new TranslationTextComponent("message.bizarremod.race_changed", d.race.displayName()), false);
        return true;
    }

    private static boolean serum(ServerPlayerEntity p) {
        JojoData d = JojoData.get(p);
        if (d.race == Race.HUMAN) {
            p.sendStatusMessage(new TranslationTextComponent("message.bizarremod.cannot_use"), true);
            return false;
        }
        d.race = Race.HUMAN;
        p.sendStatusMessage(new TranslationTextComponent("message.bizarremod.race_changed", d.race.displayName()), false);
        return true;
    }
}
