package me.kyren223.rrutils.events;

import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.utils.PlayerData;
import me.kyren223.rrutils.utils.Utils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

public class EndTickListener implements ClientTickEvents.EndTick {

    @Override
    public void onEndTick(MinecraftClient client) {
        boolean foodWasAboveHalf = PlayerData.foodLevel > 10;

        if (client == null || client.player == null) return;
        PlayerData.xpProgress = client.player.experienceProgress;
        PlayerData.xpLevel = client.player.experienceLevel;
        PlayerData.foodLevel = client.player.getHungerManager().getFoodLevel();
        PlayerData.healthShield = client.player.getAbsorptionAmount();

        if (RRUtils.CONFIG.notifyLowHunger() && foodWasAboveHalf && PlayerData.foodLevel <= 10)
            Utils.sendNotification("Low Hunger, Eat!", Formatting.GOLD);
    }
}
