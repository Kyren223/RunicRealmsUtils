/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.events;

import me.kyren223.rrutils.utils.Utils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class StartTickListener implements ClientTickEvents.StartTick {

    @Override
    public void onStartTick(MinecraftClient client) {
        if (client.player == null) return;
        Utils.updateScoreboardData(client.player);
    }
}
