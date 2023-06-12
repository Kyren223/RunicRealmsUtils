package me.kyren223.rrutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.kyren223.rrutils.utils.PlayerData;
import me.kyren223.rrutils.utils.Utils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ReplyCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        if (dispatcher == null) return;
        dispatcher.register(literal("r")
                .then(argument("message", greedyString())
                .executes(ctx -> {
                    String msg = getString(ctx, "message");
                    String command = "msg " + PlayerData.lastReply + " " + msg;
                    Utils.getPlayer().networkHandler.sendChatCommand(command);
                    return 0;
                })));
    }
}
