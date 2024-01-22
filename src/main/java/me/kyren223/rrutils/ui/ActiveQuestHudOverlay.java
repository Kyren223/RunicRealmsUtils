package me.kyren223.rrutils.ui;

import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.utils.PlayerData;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ActiveQuestHudOverlay implements HudRenderCallback {
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        if (!RRUtils.CONFIG.showActiveQuest()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        
        // Render Active Quest
        if (PlayerData.activeQuest != null) {
            int offset = 0;
            for (Text text : PlayerData.activeQuest) {
                if (text.toString().isEmpty()) continue;
                offset += 10;
                
                // Draw Text
                DrawableHelper.drawTextWithShadow(matrixStack, client.textRenderer, text,
                        RRUtils.CONFIG.activeQuestX(),
                        RRUtils.CONFIG.activeQuestY() + offset, 11);
            }
        }
    }
}
