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
import net.minecraft.scoreboard.ScoreboardPlayerScore;
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

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        if (!RRUtils.CONFIG.renderHud()) return;
        if (!RRUtils.CONFIG.renderInfo()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;


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

            List<ScoreboardPlayerScore> scores = new ArrayList<>(scoreboard.getAllPlayerScores(objective));
            Collections.reverse(scores);
            for (ScoreboardPlayerScore score : scores) {
                if (score.getScore() <= 8 && score.getScore() >= 4) {
                    offset += 10;

                    // Draw Text
                    DrawableHelper.drawTextWithShadow(matrixStack, client.textRenderer,
                            score.getPlayerName(), RRUtils.CONFIG.showInfoX() + 15,
                            RRUtils.CONFIG.showInfoY() + offset + 15, 11);
                }
            }
        }
    }
}
