/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.core;

import com.sun.jna.CallbackResultContext;
import me.kyren223.rrutils.commands.RRUtilsCommand;
import me.kyren223.rrutils.events.EndTickListener;
import me.kyren223.rrutils.events.KeyInputHandler;
import me.kyren223.rrutils.events.ModifyChatListener;
import me.kyren223.rrutils.ui.*;
import me.kyren223.rrutils.utils.PlayerData;
import me.kyren223.rrutils.utils.Utils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.TypedActionResult;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class RRUtilsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RRUtils.LOGGER.info("Client Initialized");
        KeyInputHandler.register();
        registerEvents();
        registerCommands();
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> RRUtilsCommand.register(dispatcher));
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
        ClientTickEvents.END_CLIENT_TICK.register(new EndTickListener());
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack itemStack = player.getStackInHand(hand);
            String name = itemStack.getName().getString().toUpperCase();
            if (isPotion(name)) {
                if (shouldCancel(name)) {
                    Utils.sendMessage("Cannot use potion", Formatting.RED);
                    return TypedActionResult.fail(itemStack);
                }
            }
            return TypedActionResult.pass(itemStack);
        });
    }

    private boolean isPotion(String name) {
        return name.contains("POTION");
    }

    private boolean shouldCancel(String name) {
        int amount = -1;
        if (name.contains("MINOR")) amount = 75;
        if (name.contains("MAJOR")) amount = 125;
        if (name.contains("GREATER")) amount = 225;

        int min = name.contains("HEALTH") ? PlayerData.health : PlayerData.mana;
        int max = name.contains("HEALTH") ? PlayerData.maxHealth : PlayerData.maxMana;

        switch (RRUtils.CONFIG.potionConsumeType()) {
            case NORMAL -> { return false; }
            case THRESHOLD -> {
                int percentage = (int) (((double) min / max) * 100);
                int treshold = RRUtils.CONFIG.potionConsumeThreshold();
                return percentage >= treshold;
            }

            case EFFICIENT -> {
                if (amount == -1) return false;
                return amount + min > max;
            }
        }
        return false;
    }
}

