package com.teri.alttd.Utilities;

import net.dv8tion.jda.api.entities.PrivateChannel;

import java.text.MessageFormat;

public class NoPermission {
    public enum Permission{
        WRITE(net.dv8tion.jda.api.Permission.MESSAGE_WRITE, "I can't write in {0} in {1}");

        private final net.dv8tion.jda.api.Permission permission;
        private final String message;

        Permission(net.dv8tion.jda.api.Permission permission, String message){
            this.message = message;
            this.permission = permission;
        }

        public Permission getMessage(net.dv8tion.jda.api.Permission permission) {
            for (Permission perm : Permission.values()){
                if (perm.permission.equals(permission)){
                    return perm;
                }
            }
            return null;
        }
    }

    public static void sendMessage(PrivateChannel channel, Permission permission, String guildName, String channelName){
        if (permission == null){
            channel.sendMessage("I'm missing some permission in the guild: " + guildName + " in the channel: " + channelName).queue();
            return;
        }
        channel.sendMessage(MessageFormat.format(permission.message, guildName, channelName)).queue();
    }
}
