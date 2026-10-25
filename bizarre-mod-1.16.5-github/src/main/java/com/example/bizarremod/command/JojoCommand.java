package com.example.bizarremod.command;

import com.example.bizarremod.data.FightStyle;
import com.example.bizarremod.data.JojoData;
import com.example.bizarremod.data.Race;
import com.example.bizarremod.data.StandType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

/** /jojo race <id> | /jojo style <id> | /jojo stand <id>   (solo operatori) */
public class JojoCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("jojo")
                .requires(s -> s.hasPermissionLevel(2))
                .then(branch("race", Race.values(), r -> r.id, (d, r) -> d.race = r))
                .then(branch("style", FightStyle.values(), s -> s.id, (d, s) -> d.style = s))
                .then(branch("stand", StandType.values(), s -> s.id, (d, s) -> {
                    d.stand = s;
                    d.standActive = false;
                    d.selectedMove = 0;
                })));
    }

    private static <E> LiteralArgumentBuilder<CommandSource> branch(
            final String name, final E[] values, final Function<E, String> id, final BiConsumer<JojoData, E> setter) {
        return Commands.literal(name).then(Commands.argument("value", StringArgumentType.word())
                .suggests((ctx, builder) ->
                        ISuggestionProvider.suggest(Arrays.stream(values).map(id), builder))
                .executes(ctx -> {
                    String wanted = StringArgumentType.getString(ctx, "value");
                    ServerPlayerEntity p = ctx.getSource().asPlayer();
                    for (E candidate : values) {
                        if (id.apply(candidate).equals(wanted)) {
                            setter.accept(JojoData.get(p), candidate);
                            ctx.getSource().sendFeedback(new StringTextComponent(name + " = " + wanted), false);
                            return 1;
                        }
                    }
                    ctx.getSource().sendErrorMessage(new StringTextComponent("Valore non valido: " + wanted));
                    return 0;
                }));
    }
}
