/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
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
    private static final String LEVEL_KEYWORD = "lv.";

    private static void updateHealth(String s) {
        String cleanedInput = s.replaceAll("[^\\d/]+", "");

        // Split the cleaned string using the slash as the delimiter
        String[] numbers = cleanedInput.split("/");

        if (numbers.length == 2) {
            PlayerData.health = Integer.parseInt(numbers[0].trim().substring(1));
            PlayerData.maxHealth = Integer.parseInt(numbers[1].trim());
        }
    }

    private static void updateMana(String s) {
        String cleanedInput = s.replaceAll("[^\\d/]+", "");

        // Split the cleaned string using the slash as the delimiter
        String[] numbers = cleanedInput.split("/");

        if (numbers.length == 2) {
            PlayerData.mana = Integer.parseInt(numbers[0].trim().substring(1));
            PlayerData.maxMana = Integer.parseInt(numbers[1].trim().substring(1));
        }
    }

    public static void updateScoreboardData(ClientPlayerEntity player) {
        // Update health and mana from scoreboard
        Scoreboard scoreboard = player.getScoreboard();
        Collection<ScoreboardObjective> objectives = scoreboard.getObjectives();
        for (ScoreboardObjective objective : objectives) {
            Collection<ScoreboardPlayerScore> scores = scoreboard.getAllPlayerScores(objective);
            for (ScoreboardPlayerScore score : scores) {
                String s = score.getPlayerName();

                if (s.contains(HEALTH__KEYWORD)) Utils.updateHealth(s);
                if (s.contains(MANA_KEYWORD)) Utils.updateMana(s);
                if (s.contains(LEVEL_KEYWORD)) Utils.updateLevel(s);
            }
        }
    }

    private static void updateLevel(String s) {
        char[] chars = s.toCharArray();
        char ones = chars[chars.length - 1];
        char tens = chars[chars.length - 2];
        int level = 0;
        if (Character.isDigit(ones)) level = ones - '0';
        if (Character.isDigit(tens)) level += 10 * (tens - '0');

        PlayerData.level = level;
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
            if (displayName.contains(Text.of("❤"))) {
                members.add(displayName);
            }
        }

        if (!members.isEmpty()) {
            members.add(0, getPlayer().getName().copy().formatted(Formatting.GREEN)
                    .append(MutableText.of(new LiteralTextContent(" (you)"))
                            .formatted(Formatting.GRAY)));
        }

        return members;
    }
}
