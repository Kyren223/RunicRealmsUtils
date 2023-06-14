/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.utils;

import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.ui.InfoHudOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    public static List<Text> getPartyMembers() {
        Collection<PlayerListEntry> entries = Utils.getPlayer().networkHandler.getListedPlayerListEntries();
        List<Text> members = new ArrayList<>();

        // Get all party members
        for (PlayerListEntry entry : entries)
        {
            Text displayName = entry.getDisplayName();
            if (displayName == null) continue;
            if (displayName.getString().contains("❤")) {
                members.add(
                        MutableText.of(new LiteralTextContent("§a"))
                                .formatted(Formatting.GREEN).append(displayName));
            }
        }

        return members;
    }

    public static void sendMessage(Text text) {
        Utils.getPlayer().sendMessage(
                MutableText.of(new LiteralTextContent("[RRUtils] "))
                .formatted(Formatting.LIGHT_PURPLE).append(text));
    }

    public static void sendMessage(String s, Formatting color) {
        sendMessage(MutableText.of(new LiteralTextContent(s)).formatted(color));
    }

    public static void sendMessage(String s) {
        sendMessage(s, Formatting.GRAY);
    }

    public static void sendNotification(String s, Formatting color) {
        MinecraftClient.getInstance().inGameHud.setTitle(
                MutableText.of(new LiteralTextContent(s)).formatted(color));
    }

    public static void simulateRightClick(int duration) {
        MinecraftClient.getInstance().options.useKey.setPressed(true);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> MinecraftClient.getInstance().options.useKey.setPressed(false);
        executor.schedule(task, duration, TimeUnit.MILLISECONDS);
        executor.shutdown();
    }
}
