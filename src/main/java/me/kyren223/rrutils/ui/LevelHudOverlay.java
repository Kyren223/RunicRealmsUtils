/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.utils.PlayerData;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class LevelHudOverlay implements HudRenderCallback {
    private static final int BAR_OFFSET_X = -91+81+2-1;
    private static final int BAR_OFFSET_Y = -42 -4-1;
    private static final int BAR_WIDTH = 18;
    private static final int BAR_HEIGHT = 18;

    private static final Identifier EMPTY_BAR = new Identifier(RRUtils.MOD_ID,
            "textures/hud/empty_level_bar.png");
    private static final Identifier FILLED_BAR = new Identifier(RRUtils.MOD_ID,
            "textures/hud/filled_level_bar.png");

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        if (!RRUtils.CONFIG.renderHud()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        // Get screen size
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        int x = width / 2;
        int y = height;

        // Set up rendering
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        // Render filled portion
        RenderSystem.setShaderTexture(0, FILLED_BAR);
        DrawableHelper.drawTexture(matrixStack,
                x + BAR_OFFSET_X, y + BAR_OFFSET_Y,
                0, 0, BAR_WIDTH, BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);

        // Calculate amount to fill
        int filled = (int) (PlayerData.xpProgress * BAR_HEIGHT);

        // Render unfilled portion
        RenderSystem.setShaderTexture(0, EMPTY_BAR);
        DrawableHelper.drawTexture(matrixStack,
                x + BAR_OFFSET_X, y + BAR_OFFSET_Y,
                0, 0, BAR_WIDTH, BAR_HEIGHT - filled, BAR_WIDTH, BAR_HEIGHT);

        // Calculate render values
        int level = PlayerData.xpLevel;
        MutableText text = MutableText.of(new LiteralTextContent("" + level))
                .formatted(Formatting.GOLD);


        // Render text
        x += 8 + 1;
        y += 4 + 1;
        DrawableHelper.drawCenteredTextWithShadow(matrixStack, client.textRenderer,
                text, x + BAR_OFFSET_X, y + BAR_OFFSET_Y, 0);
    }
}
