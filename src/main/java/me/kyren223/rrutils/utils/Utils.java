/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.utils;

import me.kyren223.rrutils.ui.InfoHudOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {
    public static final String HEALTH__KEYWORD = "Health";
    public static final String MANA_KEYWORD = "Mana";

    private static void updateHealth(String s) {
        String cleanedInput = s.replaceAll("[^\\d/]+", "");

        // Split the cleaned string using the slash as the delimiter
        String[] numbers = cleanedInput.split("/");

        if (numbers.length == 2) {
            PlayerData.health = Integer.parseInt(numbers[0].trim());
            PlayerData.maxHealth = Integer.parseInt(numbers[1].trim());
        }
    }

    private static void updateMana(String s) {
        String cleanedInput = s.replaceAll("[^\\d/]+", "");

        // Split the cleaned string using the slash as the delimiter
        String[] numbers = cleanedInput.split("/");

        if (numbers.length == 2) {
            PlayerData.mana = Integer.parseInt(numbers[0].trim());
            PlayerData.maxMana = Integer.parseInt(numbers[1].trim());
        }
    }

    public static void updateScoreboardData(ScoreboardObjective objective) {
        // Update health and mana from scoreboard
        Collection<ScoreboardPlayerScore> scores = objective.getScoreboard().getAllPlayerScores(objective);
        InfoHudOverlay.info.clear();
        for (ScoreboardPlayerScore score : scores) {
            String s = score.getPlayerName();
            Team team = objective.getScoreboard().getPlayerTeam(s);
            if (team == null) continue;

            String prefix = team.getPrefix().getString();
            if (prefix.contains(HEALTH__KEYWORD)) Utils.updateHealth(prefix);
            else if (prefix.contains(MANA_KEYWORD)) Utils.updateMana(prefix);
            else InfoHudOverlay.info.add(prefix);
        }
    }

    public static ClientPlayerEntity getPlayer() {
        return MinecraftClient.getInstance().player;
    }

    public static void sendMessage(String s) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.sendMessage(Text.of(s));
    }

    public static List<Text> getPartyMembers() {
        Collection<PlayerListEntry> entries = Utils.getPlayer().networkHandler.getListedPlayerListEntries();
        List<Text> members = new ArrayList<>();

        // Get all party members
        for (PlayerListEntry entry : entries)
        {
            Text displayName = entry.getDisplayName();
            if (displayName == null) continue;
            if (displayName.getString().contains("❤")) {
                if (entry.getProfile().getId().equals(getPlayer().getGameProfile().getId())) {
                    members.add(0, getPlayer().getName().copy().formatted(Formatting.GREEN)
                            .append(MutableText.of(new LiteralTextContent(" (you)"))
                                    .formatted(Formatting.GRAY)));
                }
                else members.add(displayName);
            }
        }

        return members;
    }
}
