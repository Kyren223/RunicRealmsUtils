/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.data.TextRenderLocation;
import me.kyren223.rrutils.data.TextRenderType;
import me.kyren223.rrutils.utils.PlayerData;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ManaHudOverlay implements HudRenderCallback {
    private static final int BAR_OFFSET_X = 10;
    private static final int BAR_OFFSET_Y = -42;
    private static final int BAR_WIDTH = 81;
    private static final int BAR_HEIGHT = 12;

    private static final Identifier EMPTY_BAR = new Identifier(RRUtils.MOD_ID,
            "textures/hud/empty_mana_bar.png");
    private static final Identifier FILLED_BAR = new Identifier(RRUtils.MOD_ID,
            "textures/hud/filled_mana_bar.png");

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

        // Render empty bar
        RenderSystem.setShaderTexture(0, EMPTY_BAR);
        DrawableHelper.drawTexture(matrixStack,
                x + BAR_OFFSET_X, y + BAR_OFFSET_Y,
                0, 0, BAR_WIDTH, BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);

        // Calculate amount to fill
        float percentage = (float) PlayerData.mana / PlayerData.maxMana;
        float pixelPercentage = BAR_WIDTH - 2 / 100f;
        int filled = (int) (percentage * pixelPercentage);

        // Render filled bar
        RenderSystem.setShaderTexture(0, FILLED_BAR);
        DrawableHelper.drawTexture(matrixStack,
                x + BAR_OFFSET_X, y + BAR_OFFSET_Y,
                0, 0, filled, BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);

        // Calculate render values
        String addedSymbol = "";
        if (RRUtils.CONFIG.hudTextRenderType() == TextRenderType.NO_RENDER) return;
        int current = 0, max = 0;
        if (RRUtils.CONFIG.hudTextRenderType() == TextRenderType.PERCENTAGE_RENDER) {
            current = (int) (percentage * 100);
            max = 100;
            addedSymbol = "%";
        } else if (RRUtils.CONFIG.hudTextRenderType() == TextRenderType.EXACT_RENDER) {
            current = PlayerData.mana;
            max = PlayerData.maxMana;
        }
        MutableText left = MutableText.of(new LiteralTextContent("" + current + addedSymbol))
                .formatted(Formatting.AQUA);
        MutableText middle = MutableText.of(new LiteralTextContent(" / "))
                .formatted(Formatting.YELLOW);
        MutableText right = MutableText.of(new LiteralTextContent("" + max + addedSymbol))
                .formatted(Formatting.AQUA);
        Text combined = left.append(middle).append(right);


        // Render text
        y += 2;
        x += 40;
        if (RRUtils.CONFIG.hudTextRenderLocation() == TextRenderLocation.ABOVE) y -= 12;
        DrawableHelper.drawCenteredTextWithShadow(matrixStack, client.textRenderer,
                combined, x + BAR_OFFSET_X, y + BAR_OFFSET_Y, 11);
    }
}
