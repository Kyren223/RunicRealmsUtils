/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.events;

import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.data.KeyAction;
import me.kyren223.rrutils.utils.Utils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {

    public static final String KEY_CATEGORY = "key.category.rrutils";
    public static final String KEY_TOGGLE_HUD = "key.rrutils.toggle_hud";
    public static final String KEY_SHOW_INFO = "key.rrutils.show_info";
    public static final String KEY_RIDE_MOUNT = "key.rrutils.ride_mount";
    public static final String KEY_OPEN_QUESTBOOK = "key.rrutils.open_questbook";
    public static final String KEY_OPEN_RUNESTONE = "key.rrutils.open_runestone";
    public static final String KEY_USE_HEARTSTONE = "key.rrutils.use_heartstone";

    public static KeyBinding toggleHudkey;
    public static KeyBinding showInfoKey;
    public static KeyBinding rideMountKey;
    public static KeyBinding openQuestbook;
    public static KeyBinding openRunestone;
    public static KeyBinding useHeartstone;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleHudkey.wasPressed()) onToggleHudKeyPressed(client.player);
            if (showInfoKey.wasPressed()) onShowInfoKeyPressed();
            else onShowInfoKeyReleased();
            if (rideMountKey.isPressed()) onRideMountKeyPressed(client.player);
            if (openQuestbook.isPressed()) onOpenQuestbookPressed(client.player);
            if (openRunestone.isPressed()) onOpenRunestonePressed(client.player);
            if (useHeartstone.isPressed()) onUseHeartstonePressed(client.player);
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
        PlayerInventory inv = player.getInventory();
        inv.selectedSlot = RRUtils.CONFIG.mountHotbarSlot() - 1; // -1 for 0-indexing;
        Utils.simulateRightClick(50);
    }

    private static void onOpenQuestbookPressed(ClientPlayerEntity player) {
        if (player == null) return;
        player.getInventory().selectedSlot = 7; // 1 prior to last slot (8th)
        Utils.simulateRightClick(50);
    }

    private static void onOpenRunestonePressed(ClientPlayerEntity player) {
        if (player == null) return;
        player.getInventory().selectedSlot = 0; // first slot (1st)
        Utils.simulateRightClick(50);
    }

    private static void onUseHeartstonePressed(ClientPlayerEntity player) {
        if (player == null) return;
        player.getInventory().selectedSlot = 8; // last slot (9th)
        Utils.simulateRightClick(50);
    }

    private static void onPressed(ClientPlayerEntity player) {
        if (player == null) return;
    }

    public static void register() {
        toggleHudkey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
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
        registerKeyInputs();
    }

}
