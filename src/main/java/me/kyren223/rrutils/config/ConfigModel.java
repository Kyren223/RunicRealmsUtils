/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RangeConstraint;
import io.wispforest.owo.config.annotation.SectionHeader;
import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.data.KeyAction;
import me.kyren223.rrutils.data.PotionConsumeType;
import me.kyren223.rrutils.data.TextRenderLocation;
import me.kyren223.rrutils.data.TextRenderType;

@SuppressWarnings("unused")
@Modmenu(modId = RRUtils.MOD_ID)
@Config(name = "rru-config", wrapperName = "RRUtilsConfig")
public class ConfigModel {

    @SectionHeader("Toggles")
    public boolean renderHud = true;
    public boolean disableScoreboard = false;
    public boolean renderInfo = false;
    public boolean renderShowInfoBackground = true;
    public boolean showParty = true;
    public boolean showActiveQuest = true;

    @SectionHeader("Modes")
    public TextRenderType hudTextRenderType = TextRenderType.EXACT_RENDER;
    public TextRenderLocation hudTextRenderLocation = TextRenderLocation.ABOVE;
    public KeyAction showInfoKeyAction = KeyAction.TOGGLE;

    @SectionHeader("Positions")
    public int showInfoX = 530;
    public int showInfoY = 210;
    public int partyX = 5;
    public int partyY = 130;
    public int partyOpacity = 100;
    public int activeQuestX = 5;
    public int activeQuestY = 160;

    @SectionHeader("Utilities")
    public PotionConsumeType potionConsumeType = PotionConsumeType.EFFICIENT;
    @RangeConstraint(min = 1, max = 100) public int potionConsumeThreshold = 90;
    public boolean safeDrop = true;
    public boolean notifyLowHunger = true;
}
