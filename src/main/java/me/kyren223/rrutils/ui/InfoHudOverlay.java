/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.utils.Utils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InfoHudOverlay implements HudRenderCallback {
    private static final int BAR_WIDTH = 106;
    private static final int BAR_HEIGHT = 122;
    private static final Identifier BACKGROUND = new Identifier(RRUtils.MOD_ID,
            "textures/background/info_background.png");

    public static List<String> info = new ArrayList<>();

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        if (!RRUtils.CONFIG.renderInfo()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        if (info.isEmpty()) return;

        // Set up rendering
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        // Render Background
        if (RRUtils.CONFIG.renderShowInfoBackground()) {
            RenderSystem.setShaderTexture(0, BACKGROUND);
            DrawableHelper.drawTexture(matrixStack,
                    RRUtils.CONFIG.showInfoX(), RRUtils.CONFIG.showInfoY(),
                    0, 0, BAR_WIDTH, BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);
        }

        // Draw Information
        Scoreboard scoreboard = Utils.getPlayer().getScoreboard();
        Collection<ScoreboardObjective> objectives = scoreboard.getObjectives();
        for (ScoreboardObjective objective : objectives) {
            int offset = 15;

            // Draw Header
            DrawableHelper.drawCenteredTextWithShadow(matrixStack, client.textRenderer,
                    objective.getDisplayName(), RRUtils.CONFIG.showInfoX() + BAR_WIDTH / 2,
                     RRUtils.CONFIG.showInfoY() + offset, 11);

            Collections.reverse(info);
            for (String data : info) {
                offset += 10;

                // Draw Text
                DrawableHelper.drawTextWithShadow(matrixStack, client.textRenderer,
                        MutableText.of(new LiteralTextContent(data)).formatted(Formatting.YELLOW),
                        RRUtils.CONFIG.showInfoX() + 15,
                        RRUtils.CONFIG.showInfoY() + offset + 15, 11);
            }
        }
    }
}
