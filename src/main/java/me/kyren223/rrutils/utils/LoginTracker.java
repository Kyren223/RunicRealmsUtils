package me.kyren223.rrutils.utils;

import me.kyren223.rrutils.core.RRUtils;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoginTracker {
    /*
    private static final List<String> trackedPlayers = new ArrayList<>();

    public static void updateTrackedPlayers() {
        Collection<PlayerListEntry> entries = Utils.getPlayer().networkHandler.getListedPlayerListEntries();
        List<String> names = new ArrayList<>();

        for (PlayerListEntry entry : entries)
        {
            Text displayName = entry.getDisplayName();
            if (displayName == null) continue;
            String name = displayName.getString().trim();
            names.add(name);

            // Player is in the list
            if (!RRUtils.CONFIG.playerLoginTrackerList().contains(name)) continue;
            if (trackedPlayers.contains(name)) continue;

            // Player just logged on
            trackedPlayers.add(name);
            Utils.sendMessage(name + " logged in!", Formatting.YELLOW);
        }

        for (String name : trackedPlayers) {
            if (!RRUtils.CONFIG.playerLoginTrackerList().contains(name)) continue;
            if (names.contains(name)) continue;
            // Player just logged off
            trackedPlayers.remove(name);
            Utils.sendMessage(name + " logged off!", Formatting.YELLOW);
        }
    }
    */
}
