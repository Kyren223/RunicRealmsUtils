package me.kyren223.rrutils.ui;

import me.kyren223.rrutils.config.ConfigModel;
import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.utils.Utils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class PartyHudOverlay implements HudRenderCallback {
    private static final int TEXT_OFFSET = 5;
    private static final int BAR_WIDTH = 122;
    private static final int BAR_HEIGHT = 10;

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        if (!RRUtils.CONFIG.renderHud()) return;
        if (!RRUtils.CONFIG.showParty()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        List<Text> members = Utils.getPartyMembers();
        if (members.isEmpty()) return;

        int color1 = (RRUtils.CONFIG.partyOpacity() << 24);
        int color2 = (RRUtils.CONFIG.partyOpacity() << 24) | (64 << 16) | (64 << 8) | 64;

        // Render Background Header
        DrawableHelper.fill(matrixStack,
                RRUtils.CONFIG.partyX(), RRUtils.CONFIG.partyY() - 1,
                RRUtils.CONFIG.partyX() + BAR_WIDTH, RRUtils.CONFIG.partyY() + BAR_HEIGHT,
                color1);

        // Render Text Header
        MutableText partyHeader = MutableText.of(new LiteralTextContent("Party Members"))
                .formatted(Formatting.GREEN).formatted(Formatting.BOLD);

        DrawableHelper.drawCenteredTextWithShadow(matrixStack, client.textRenderer,
                partyHeader, RRUtils.CONFIG.partyX() + BAR_WIDTH / 2,
                RRUtils.CONFIG.partyY(), 11);

        // Render Background
        DrawableHelper.fill(matrixStack,
                RRUtils.CONFIG.partyX(), RRUtils.CONFIG.partyY() + BAR_HEIGHT,
                RRUtils.CONFIG.partyX() + BAR_WIDTH,
                RRUtils.CONFIG.partyY() + TEXT_OFFSET + (BAR_HEIGHT * members.size()) - 1 + BAR_HEIGHT,
                color2);

        // Render Party Members
        for (int i = 0; i < members.size(); i++) {
            // Render Text
            DrawableHelper.drawTextWithShadow(matrixStack, client.textRenderer,
                    members.get(i), RRUtils.CONFIG.partyX() + 1,
                    RRUtils.CONFIG.partyY() + TEXT_OFFSET + (BAR_HEIGHT * (i + 1)), 0);
        }
    }
}
