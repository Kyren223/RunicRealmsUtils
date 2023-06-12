package me.kyren223.rrutils.events;

import me.kyren223.rrutils.utils.LoginTracker;
import me.kyren223.rrutils.utils.PlayerData;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class EndTickListener implements ClientTickEvents.EndTick {

    @Override
    public void onEndTick(MinecraftClient client) {
        if (client == null || client.player == null) return;
        PlayerData.xpProgress = client.player.experienceProgress;
        PlayerData.xpLevel = client.player.experienceLevel;
        PlayerData.foodLevel = client.player.getHungerManager().getFoodLevel();
        PlayerData.healthShield = client.player.getAbsorptionAmount();
        LoginTracker.updateTrackedPlayers();
    }
}
