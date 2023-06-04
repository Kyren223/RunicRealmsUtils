package me.kyren223.rrutils.core;

import me.kyren223.rrutils.commands.RRUtilsCommand;
import me.kyren223.rrutils.events.KeyInputHandler;
import me.kyren223.rrutils.events.ModifyChatListener;
import me.kyren223.rrutils.events.StartTickListener;
import me.kyren223.rrutils.ui.*;
import me.kyren223.rrutils.utils.SlotManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class RRUtilsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RRUtils.LOGGER.info("Client Initialized");
        KeyInputHandler.register();
        registerEvents();
        registerCommands();
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            RRUtilsCommand.register(dispatcher);
        });
    }

    private void registerEvents() {
        // Register events
        ModifyChatListener modifyChatListener = new ModifyChatListener();
        ClientSendMessageEvents.MODIFY_CHAT.register(modifyChatListener);
        ClientReceiveMessageEvents.ALLOW_CHAT.register(modifyChatListener);
        HudRenderCallback.EVENT.register(new HealthHudOverlay());
        HudRenderCallback.EVENT.register(new ManaHudOverlay());
        HudRenderCallback.EVENT.register(new LevelHudOverlay());
        HudRenderCallback.EVENT.register(new HungerHudOverlay());
        HudRenderCallback.EVENT.register(new InfoHudOverlay());
        HudRenderCallback.EVENT.register(new PartyHudOverlay());
        ClientTickEvents.START_CLIENT_TICK.register(new StartTickListener());
    }
}
