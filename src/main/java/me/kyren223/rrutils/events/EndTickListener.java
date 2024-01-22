package me.kyren223.rrutils.events;

import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.utils.PlayerData;
import me.kyren223.rrutils.utils.Utils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class EndTickListener implements ClientTickEvents.EndTick {

    @Override
    public void onEndTick(MinecraftClient client) {
        if (client == null || client.player == null) return;
        
        boolean foodWasAboveHalf = PlayerData.foodLevel > 10;
        PlayerData.xpProgress = client.player.experienceProgress;
        PlayerData.xpLevel = client.player.experienceLevel;
        PlayerData.foodLevel = client.player.getHungerManager().getFoodLevel();
        PlayerData.healthShield = client.player.getAbsorptionAmount();

        if (RRUtils.CONFIG.notifyLowHunger() && foodWasAboveHalf && PlayerData.foodLevel <= 10) {
            Utils.sendNotification("Low Hunger, Eat!", Formatting.GOLD);
        }
        
        // Update active quest
        if (client.currentScreen instanceof HandledScreen<?> handledScreen) {
            ItemStack item = handledScreen.getScreenHandler().getCursorStack();
            if (item == null) return;
            Text name = item.getName();
            List<Text> lore = item.getTooltip(client.player, TooltipContext.Default.BASIC);
            String s = name.getString() + lore;
            if (!s.toLowerCase().contains("quest")) return;
            PlayerData.activeQuest = lore;
        }
    }
}
