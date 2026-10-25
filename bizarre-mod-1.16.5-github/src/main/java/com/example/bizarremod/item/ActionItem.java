package com.example.bizarremod.item;

import java.util.function.Predicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/** Oggetto monouso: al click destro esegue un'azione sul giocatore (se riesce, viene consumato). */
public class ActionItem extends Item {
    private final Predicate<ServerPlayerEntity> action;

    public ActionItem(Item.Properties properties, Predicate<ServerPlayerEntity> action) {
        super(properties);
        this.action = action;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) return ActionResult.resultSuccess(stack);

        if (player instanceof ServerPlayerEntity && action.test((ServerPlayerEntity) player)) {
            if (!player.abilities.isCreativeMode) stack.shrink(1);
            return ActionResult.resultSuccess(stack);
        }
        return ActionResult.resultFail(stack);
    }
}
