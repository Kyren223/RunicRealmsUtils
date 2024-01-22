/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.events;

import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.data.KeyAction;
import me.kyren223.rrutils.utils.Utils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KeyInputHandler {

    public static final String KEY_CATEGORY = "key.category.rrutils";
    public static final String KEY_TOGGLE_HUD = "key.rrutils.toggle_hud";
    public static final String KEY_SHOW_INFO = "key.rrutils.show_info";
    public static final String KEY_RIDE_MOUNT = "key.rrutils.ride_mount";
    public static final String KEY_OPEN_QUESTBOOK = "key.rrutils.open_questbook";
    public static final String KEY_OPEN_RUNESTONE = "key.rrutils.open_runestone";
    public static final String KEY_USE_HEARTSTONE = "key.rrutils.use_heartstone";
    public static final String KEY_SEND_ITEM_IN_CHAT = "key.rrutils.send_item_in_chat";
    public static final String KEY_SEND_COORDS_IN_CHAT = "key.rrutils.send_coords_in_chat";

    public static KeyBinding toggleHudKey;
    public static KeyBinding showInfoKey;
    public static KeyBinding rideMountKey;
    public static KeyBinding openQuestbook;
    public static KeyBinding openRunestone;
    public static KeyBinding useHeartstone;
    public static KeyBinding sendItemInChat;
    public static KeyBinding sendCoordsInChat;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleHudKey.wasPressed()) onToggleHudKeyPressed(client.player);
            if (showInfoKey.wasPressed()) onShowInfoKeyPressed();
            else onShowInfoKeyReleased();
            if (rideMountKey.isPressed()) onRideMountKeyPressed(client.player);
            if (openQuestbook.isPressed()) onOpenQuestbookPressed(client.player);
            if (openRunestone.isPressed()) onOpenRunestonePressed(client.player);
            if (useHeartstone.isPressed()) onUseHeartstonePressed(client.player);
            if (sendItemInChat.isPressed()) onSendItemInChatPressed(client.player);
            if (sendCoordsInChat.isPressed()) onSendCoordsInChatPressed(client.player);
        });
    }

    private static void onShowInfoKeyReleased() {
        if (RRUtils.CONFIG.showInfoKeyAction() == KeyAction.HOLD) {
            RRUtils.CONFIG.renderInfo(false);
        }
    }

    private static void onShowInfoKeyPressed() {
        if (RRUtils.CONFIG.showInfoKeyAction() == KeyAction.HOLD) {
            RRUtils.CONFIG.renderInfo(true);
        } else if (RRUtils.CONFIG.showInfoKeyAction() == KeyAction.TOGGLE) {
            RRUtils.CONFIG.renderInfo(!RRUtils.CONFIG.renderInfo());
        }
    }

    private static void onToggleHudKeyPressed(ClientPlayerEntity player) {
        if (player == null) return;
        RRUtils.CONFIG.renderHud(!RRUtils.CONFIG.renderHud());
        String isOn = RRUtils.CONFIG.renderHud() ? "ON" : "OFF";
        player.sendMessage(Text.of("HUD is now " + isOn));
    }

    private static void onRideMountKeyPressed(ClientPlayerEntity player) {
        if (player == null) return;
        player.networkHandler.sendChatCommand("/mount");
    }

    private static void onOpenQuestbookPressed(ClientPlayerEntity player) {
        if (player == null) return;
        int slot = player.getInventory().selectedSlot;
        player.getInventory().selectedSlot = 7; // 1 prior to last slot (8th)
        Utils.simulateRightClick(50);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> player.getInventory().selectedSlot = slot, 50, TimeUnit.MILLISECONDS);
        executor.shutdown();
    }

    private static void onOpenRunestonePressed(ClientPlayerEntity player) {
        if (player == null) return;
        int slot = player.getInventory().selectedSlot;
        player.getInventory().selectedSlot = 0; // first slot (1st)
        Utils.simulateRightClick(50);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> player.getInventory().selectedSlot = slot, 50, TimeUnit.MILLISECONDS);
        executor.shutdown();
    }

    private static void onUseHeartstonePressed(ClientPlayerEntity player) {
        if (player == null) return;
        int slot = player.getInventory().selectedSlot;
        player.getInventory().selectedSlot = 8; // last slot (9th)
        Utils.simulateRightClick(50);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> player.getInventory().selectedSlot = slot, 50, TimeUnit.MILLISECONDS);
        executor.shutdown();
    }
    
    private static void onSendItemInChatPressed(ClientPlayerEntity player) {
        if (player == null) return;
        player.networkHandler.sendChatMessage("[item]");
    }
    
    private static void onSendCoordsInChatPressed(ClientPlayerEntity player) {
        if (player == null) return;
        player.networkHandler.sendChatMessage("[coords]");
    }

    public static void register() {
        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_TOGGLE_HUD,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                KEY_CATEGORY));
        showInfoKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SHOW_INFO,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                KEY_CATEGORY));
        rideMountKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_RIDE_MOUNT,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                KEY_CATEGORY));
        openQuestbook = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_OPEN_QUESTBOOK,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                KEY_CATEGORY));
        openRunestone = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_OPEN_RUNESTONE,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                KEY_CATEGORY));
        useHeartstone = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_USE_HEARTSTONE,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                KEY_CATEGORY));
        sendItemInChat = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SEND_ITEM_IN_CHAT,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_T | GLFW.GLFW_MOD_SHIFT,
                KEY_CATEGORY));
        sendCoordsInChat = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SEND_COORDS_IN_CHAT,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C | GLFW.GLFW_MOD_SHIFT,
                KEY_CATEGORY));
        registerKeyInputs();
    }

}
