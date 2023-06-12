/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.utils.PlayerData;
import me.kyren223.rrutils.utils.Utils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HungerHudOverlay implements HudRenderCallback {
    private static final int BAR_OFFSET_X = -91;
    private static final int BAR_OFFSET_Y = -29;
    private static final int BAR_WIDTH = 81 * 2 + 20;
    private static final int BAR_HEIGHT = 5;

    private static final Identifier EMPTY_BAR = new Identifier(RRUtils.MOD_ID,
            "textures/hud/empty_hunger_bar.png");
    private static final Identifier FILLED_BAR = new Identifier(RRUtils.MOD_ID,
            "textures/hud/filled_hunger_bar.png");

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        if (!RRUtils.CONFIG.renderHud()) return;
        MinecraftClient client = MinecraftClient.getInstance();

        if (client == null) return;

        // Get screen size
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        int x = width / 2;

        // Set up rendering
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        // Render empty bar
        RenderSystem.setShaderTexture(0, EMPTY_BAR);
        DrawableHelper.drawTexture(matrixStack,
                x + BAR_OFFSET_X, height + BAR_OFFSET_Y,
                0, 0, BAR_WIDTH, BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);

        // Calculate amount to fill
        int filled = (int) ((BAR_WIDTH / 20f) * PlayerData.foodLevel);

        // Render filled bar
        RenderSystem.setShaderTexture(0, FILLED_BAR);
        DrawableHelper.drawTexture(matrixStack,
                x + BAR_OFFSET_X, height + BAR_OFFSET_Y,
                0, 0, filled, BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);
    }
}
