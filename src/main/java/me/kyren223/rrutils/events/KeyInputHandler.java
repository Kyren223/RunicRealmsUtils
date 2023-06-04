package me.kyren223.rrutils.events;

import me.kyren223.rrutils.config.ConfigModel;
import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.data.KeyAction;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {

    public static final String KEY_CATEGORY = "key.category.rrutils";
    public static final String KEY_TOGGLE_HUD = "key.rrutils.toggle_hud";
    public static final String KEY_SHOW_INFO = "key.rrutils.show_info";

    public static KeyBinding toggleHudkey;
    public static KeyBinding showInfoKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleHudkey.wasPressed()) onToggleHudKeyPressed(client.player);
            if (showInfoKey.wasPressed()) onShowInfoKeyPressed();
            else onShowInfoKeyReleased();
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
        registerKeyInputs();
    }

}
