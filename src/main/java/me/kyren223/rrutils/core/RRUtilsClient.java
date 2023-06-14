/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.core;

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
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;

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
        ClientSendMessageEvents.MODIFY_COMMAND.register(modifyChatListener);
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
            if (checkPotion(player, hand) == ActionResult.FAIL)
                return TypedActionResult.fail(itemStack);
            else return TypedActionResult.pass(itemStack);
        });
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> checkPotion(player, hand));
    }

    private ActionResult checkPotion(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        String name = itemStack.getName().getString().toUpperCase();
        if (isPotion(name)) {
            if (shouldCancel(name)) {
                Utils.sendMessage("Cannot use potion", Formatting.RED);
                return ActionResult.FAIL;
            }
        }
        return ActionResult.PASS;
    }

    private boolean isPotion(String name) {
        return name.contains("POTION");
    }

    private boolean shouldCancel(String name) {
        int amount = -1;
        if (name.contains("LESSER") && name.contains("CRAFTED") && name.contains("HEAL")) amount = 125;
        else if (name.contains("LESSER") && name.contains("CRAFTED") && name.contains("MANA")) amount = 75;

        else if (name.contains("MINOR") && name.contains("CRAFTED") && name.contains("HEAL")) amount = 275;
        else if (name.contains("MINOR") && name.contains("CRAFTED") && name.contains("MANA")) amount = 125;

        else if (name.contains("MAJOR") && name.contains("CRAFTED") && name.contains("HEAL")) amount = 500;
        else if (name.contains("MAJOR") && name.contains("CRAFTED") && name.contains("MANA")) amount = 250;

        else if (name.contains("GREATER") && name.contains("CRAFTED") && name.contains("HEAL")) amount = 825;
        else if (name.contains("GREATER") && name.contains("CRAFTED") && name.contains("MANA")) amount = 400;

        else if (name.contains("MINOR")) amount = 75;
        else if (name.contains("MAJOR")) amount = 125;
        else if (name.contains("GREATER")) amount = 225;

        int min = name.contains("HEAL") ? PlayerData.health : PlayerData.mana;
        int max = name.contains("HEAL") ? PlayerData.maxHealth : PlayerData.maxMana;

        switch (RRUtils.CONFIG.potionConsumeType()) {
            case NORMAL -> { return false; }
            case THRESHOLD -> {
                int percentage = (int) (((double) min / max) * 100);
                int threshold = RRUtils.CONFIG.potionConsumeThreshold();
                return percentage >= threshold;
            }

            case EFFICIENT -> {
                if (amount == -1) return false;
                return amount + min > max;
            }
        }
        return false;
    }
}

