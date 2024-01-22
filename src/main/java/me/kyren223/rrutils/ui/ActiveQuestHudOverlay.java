package me.kyren223.rrutils.ui;

import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.utils.PlayerData;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ActiveQuestHudOverlay implements HudRenderCallback {
    
    private static final int TEXT_OFFSET = 3;
    private static final int BAR_HEIGHT = 10;
    
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        if (!RRUtils.CONFIG.showActiveQuest() || PlayerData.activeQuest == null) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        
        int opacity = (int) (RRUtils.CONFIG.activeQuestOpacity() / 100d * 255);
        int color = (opacity << 24);
        
        int i = 0;
        for (Text text : PlayerData.activeQuest) {
            if (!text.getString().isBlank()) i++;
        }
        
        // Render Background
        DrawableHelper.fill(matrixStack,
                RRUtils.CONFIG.activeQuestX(), RRUtils.CONFIG.activeQuestY() + BAR_HEIGHT,
                RRUtils.CONFIG.activeQuestX() + (TEXT_OFFSET * 2) + RRUtils.CONFIG.activeQuestWidth(),
                RRUtils.CONFIG.activeQuestY() + (TEXT_OFFSET * 2) + (BAR_HEIGHT * i) - 1 + BAR_HEIGHT,
                color);
        
        // Render Active Quest
        int offset = 0;
        for (Text text : PlayerData.activeQuest) {
            if (text.getString().isBlank()) continue;
            offset += 10;
            
            // Draw Text
            DrawableHelper.drawTextWithShadow(matrixStack, client.textRenderer, text,
                    RRUtils.CONFIG.activeQuestX() + TEXT_OFFSET,
                    RRUtils.CONFIG.activeQuestY() + offset + TEXT_OFFSET, 11);
        }
    }
}
