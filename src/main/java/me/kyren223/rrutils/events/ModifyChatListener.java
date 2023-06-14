/*
 * Copyright © 2023 Kyren223. All rights reserved.
 */
package me.kyren223.rrutils.events;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class ModifyChatListener implements ClientSendMessageEvents.ModifyChat, ClientReceiveMessageEvents.AllowChat, ClientSendMessageEvents.ModifyCommand {
    @Override
    public String modifySendChatMessage(String message) {
        return message;
    }

    @Override
    public boolean allowReceiveChatMessage(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp) {
        return true;
    }

    @Override
    public String modifySendCommandMessage(String command) {
        return command;
    }
}
