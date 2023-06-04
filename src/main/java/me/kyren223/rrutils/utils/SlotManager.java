package me.kyren223.rrutils.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;

import java.util.HashMap;

public class SlotManager {

    private static SlotManager INSTANCE;
    private final HashMap<Integer, Boolean> slots = new HashMap<>();

    public static SlotManager getInstance() {
        if (INSTANCE == null) INSTANCE = new SlotManager();
        return INSTANCE;
    }

    private SlotManager() {
        PlayerInventory inv = MinecraftClient.getInstance().player.getInventory();
        for (int i = 0; i < inv.main.size(); i++) slots.put(i, false);
    }

    public boolean isLocked(int slot) {
        return slots.get(slot);
    }

    public void lock(int slot) {
        slots.put(slot, true);
    }

    public void unlock(int slot) {
        slots.put(slot, false);
    }

    public void flip(int slot) {
        slots.put(slot, slots.get(slot));
    }

}
