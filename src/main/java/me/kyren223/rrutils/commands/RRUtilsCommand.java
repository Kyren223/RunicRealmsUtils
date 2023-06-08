/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.kyren223.rrutils.core.RRUtils;
import me.kyren223.rrutils.data.KeyAction;
import me.kyren223.rrutils.data.TextRenderLocation;
import me.kyren223.rrutils.data.TextRenderType;
import me.kyren223.rrutils.utils.PlayerData;
import me.kyren223.rrutils.utils.Utils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Random;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.minecraft.command.CommandSource.suggestMatching;


public class RRUtilsCommand {


    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        if (dispatcher == null) return;

        String[] typeSuggestions = new String[3];
        typeSuggestions[0] = TextRenderType.EXACT_RENDER.toString();
        typeSuggestions[1] = TextRenderType.PERCENTAGE_RENDER.toString();
        typeSuggestions[2] = TextRenderType.NO_RENDER.toString();

        String[] locationSuggestions = new String[2];
        locationSuggestions[0] = TextRenderLocation.ABOVE.toString();
        locationSuggestions[1] = TextRenderLocation.INSIDE.toString();

        String[] keyActionSuggestions = new String[2];
        keyActionSuggestions[0] = KeyAction.TOGGLE.toString();
        keyActionSuggestions[1] = KeyAction.HOLD.toString();

        dispatcher.register(literal("rrutils")

        // debug
        .then(literal("debug")
                .executes(ctx -> debug(ctx.getSource())))

        // setHudTextRenderType
        .then(literal("setHudTextRenderType")
                .executes(ctx -> showGamerule("setHudTextRenderType", RRUtils.CONFIG.hudTextRenderType().toString()))
                .then(argument("type", word())
                .suggests((ctx, builder) -> suggestMatching(typeSuggestions, builder))
                .executes(ctx -> setHudTextRenderType(getString(ctx, "type")))))

        // setHudTextRenderLocation
        .then(literal("setHudTextRenderLocation")
                .executes(ctx -> showGamerule("setHudTextRenderLocation", RRUtils.CONFIG.hudTextRenderLocation().toString()))
                .then(argument("location", string())
                .suggests((ctx, builder) -> suggestMatching(locationSuggestions, builder))
                .executes(ctx -> setHudTextRenderLocation(getString(ctx, "location")))))

        // disableScoreboard
        .then(literal("disableScoreboard")
                .executes(ctx -> showGamerule("disableScoreboard", "" + RRUtils.CONFIG.disableScoreboard()))
                .then(argument("disable", bool())
                .executes(ctx -> disableScoreboard(getBool(ctx, "disable")))))

        // setShowInfoKeyAction
        .then(literal("setShowInfoKeyAction")
                .executes(ctx -> showGamerule("setShowInfoKeyAction", RRUtils.CONFIG.showInfoKeyAction().toString()))
                .then(argument("key_action", string())
                .suggests((ctx, builder) -> suggestMatching(keyActionSuggestions, builder))
                .executes(ctx -> setShowInfoKeyAction(getString(ctx, "key_action")))))

        // setShowInfoX
        .then(literal("setShowInfoX")
                .executes(ctx -> showGamerule("setShowInfoX", "" + RRUtils.CONFIG.showInfoX()))
                .then(argument("offset", integer())
                .suggests((ctx, builder) -> suggestMatching(new String[]{"530"}, builder))
                .executes(ctx -> setShowInfoX(getInteger(ctx, "offset")))))

        // setShowInfoY
        .then(literal("setShowInfoY")
                .executes(ctx -> showGamerule("setShowInfoY", "" + RRUtils.CONFIG.showInfoY()))
                .then(argument("offset", integer())
                .suggests((ctx, builder) -> suggestMatching(new String[]{"210"}, builder))
                .executes(ctx -> setShowInfoY(getInteger(ctx, "offset")))))

        // renderShowInfoBackground
        .then(literal("renderShowInfoBackground")
                .executes(ctx -> showGamerule("renderShowInfoBackground", "" + RRUtils.CONFIG.renderShowInfoBackground()))
                .then(argument("background", bool())
                .executes(ctx -> renderShowInfoBackground(getBool(ctx, "background")))))

        // setPartyKeyAction
        .then(literal("showParty")
                .executes(ctx -> showGamerule("showParty", "" + RRUtils.CONFIG.showParty()))
                .then(argument("show_party", bool())
                .executes(ctx -> showParty(getBool(ctx, "show_party")))))

        // setPartyX
        .then(literal("setPartyX")
                .executes(ctx -> showGamerule("setPartyX", "" + RRUtils.CONFIG.partyX()))
                .then(argument("offset", integer())
                .suggests((ctx, builder) -> suggestMatching(new String[]{"5"}, builder))
                .executes(ctx -> setPartyX(getInteger(ctx, "offset")))))

        // setPartyY
        .then(literal("setPartyY")
                .executes(ctx -> showGamerule("setPartyY", "" + RRUtils.CONFIG.partyY()))
                .then(argument("offset", integer())
                .suggests((ctx, builder) -> suggestMatching(new String[]{"130"}, builder))
                .executes(ctx -> setPartyY(getInteger(ctx, "offset")))))

        // setPartyY
        .then(literal("setPartyOpacity")
                .executes(ctx -> showGamerule("setPartyOpacity", "" + RRUtils.CONFIG.partyOpacity()))
                .then(argument("opacity", integer(0, 255))
                .suggests((ctx, builder) -> suggestMatching(new String[]{"100"}, builder))
                .executes(ctx -> setPartyOpacity(getInteger(ctx, "opacity")))))

        // coinflip
        .then(literal("coinflip")
                .executes(ctx -> coinflip(ctx.getSource(), null))));

    }

    private static int showGamerule(String gamerule, String value) {
        Utils.sendMessage(gamerule + " is currently set to: " + value);
        return 0;
    }

    private static void setGamerule(String gamerule, String value) {
        Utils.sendMessage(gamerule + " is now set to: " + value);
    }

    private static int coinflip(FabricClientCommandSource source, Boolean isHead) {
        ClientPlayerEntity player = source.getPlayer();
        if (isHead != null) {
            String choice = isHead ? "Heads!" : "Tails!";
            player.sendMessage(Text.of("You chose" + choice));
        }
        String result = new Random().nextBoolean() ? "Heads!" : "Tails!";
        player.sendMessage(Text.of("Flipped a coin: " + result));
        return 0;
    }

    private static int debug(FabricClientCommandSource source) {
        Collection<PlayerListEntry> entries = source.getPlayer().networkHandler.getListedPlayerListEntries();

        for (PlayerListEntry entry : entries)
        {
            Text displayName = entry.getDisplayName();
            if (displayName == null) continue;
            source.getPlayer().sendMessage(displayName);
        }

        String stats = """
                Health: %h / %H
                Mana: %m / %M
                """
                .replace("%h", "" + PlayerData.health)
                .replace("%H", "" + PlayerData.maxHealth)
                .replace("%m", "" + PlayerData.mana)
                .replace("%M", "" + PlayerData.maxMana);
        source.getPlayer().sendMessage(Text.of(stats));
        return 0;
    }

    private static int setHudTextRenderType(String type) {
        if (type.equalsIgnoreCase("EXACT_RENDER"))
            RRUtils.CONFIG.hudTextRenderType(TextRenderType.EXACT_RENDER);
        else if (type.equalsIgnoreCase("PERCENTAGE_RENDER"))
            RRUtils.CONFIG.hudTextRenderType(TextRenderType.PERCENTAGE_RENDER);
        else if (type.equalsIgnoreCase("NO_RENDER"))
            RRUtils.CONFIG.hudTextRenderType(TextRenderType.NO_RENDER);
        else return 0;
        setGamerule("setHudTextRenderType", type);
        return 0;
    }

    private static int setHudTextRenderLocation(String location) {
        if (location.equalsIgnoreCase("ABOVE"))
            RRUtils.CONFIG.hudTextRenderLocation(TextRenderLocation.ABOVE);
        else if (location.equalsIgnoreCase("INSIDE"))
            RRUtils.CONFIG.hudTextRenderLocation(TextRenderLocation.INSIDE);
        else return 0;
        setGamerule("setHudTextRenderLocation", location);
        return 0;
    }

    private static int disableScoreboard(boolean disable) {
        RRUtils.CONFIG.disableScoreboard(disable);
        setGamerule("disableScoreboard", "" + disable);
        return 0;
    }

    private static int setShowInfoKeyAction(String keyAction) {
        if (keyAction.equalsIgnoreCase("HOLD"))
            RRUtils.CONFIG.showInfoKeyAction(KeyAction.HOLD);
        else if (keyAction.equalsIgnoreCase("TOGGLE"))
            RRUtils.CONFIG.showInfoKeyAction(KeyAction.TOGGLE);
        else return 0;
        setGamerule("setShowInfoKeyAction", keyAction);
        return 0;
    }

    private static int setShowInfoY(int offset) {
        RRUtils.CONFIG.showInfoY(offset);
        setGamerule("setShowInfoY", "" + offset);
        return 0;
    }

    private static int setShowInfoX(int offset) {
        RRUtils.CONFIG.showInfoX(offset);
        setGamerule("setShowInfoX", "" + offset);
        return 0;
    }

    private static int renderShowInfoBackground(boolean background) {
        RRUtils.CONFIG.renderShowInfoBackground(background);
        setGamerule("setShowInfoX", "" + background);
        return 0;
    }

    private static int showParty(boolean show) {
        RRUtils.CONFIG.showParty(show);
        setGamerule("showParty", "" + show);
        return 0;
    }

    private static int setPartyX(int offset) {
        RRUtils.CONFIG.partyX(offset);
        setGamerule("setPartyX", "" + offset);
        return 0;
    }

    private static int setPartyY(int offset) {
        RRUtils.CONFIG.partyY(offset);
        setGamerule("setPartyY", "" + offset);
        return 0;
    }

    private static int setPartyOpacity(int opacity) {
        RRUtils.CONFIG.partyOpacity(opacity);
        setGamerule("setPartyOpacity", "" + opacity);
        return 0;
    }


}
