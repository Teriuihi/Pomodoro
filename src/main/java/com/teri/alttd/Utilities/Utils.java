package com.teri.alttd.Utilities;

import com.teri.alttd.FileManagement.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.Color;
import java.util.ArrayList;

public class Utils {

    /**
     * Check if an object is null and if it is log it
     * @param classObject The class this function was called from
     * @param object Object to check
     * @param action Action that was taken that should have resulted in the object not being null
     * @return true if the object is null
     */
    public static boolean isNull(Object classObject, Object object, String action){
        if (object == null) {
            new Log(Log.LogType.NULL).appendLog("Unexpected null at " + classObject.getClass().getName() + " during the following action: " + action);
            return true;
        }
        return false;
    }

    /**
     * Check if self member has the specified permissions in the provided guild
     * @param guild Guild to check self member for
     * @param channel Channel to send error to
     * @param perms Perms to check for
     * @return True if we have the permissions we need
     */
    public static boolean hasPermissions(Guild guild, TextChannel channel, NeededPermissions.guildPerms perms) {
        Member member = guild.getSelfMember();

        if (member.hasPermission(Permission.ADMINISTRATOR)){
            return true;
        } else {
            ArrayList<Permission> missingPerms = new ArrayList<>();
            for (Permission permission : perms.getPermissions()){
                if (!member.hasPermission(permission)){
                    missingPerms.add(permission);
                }
            }
            if (missingPerms.isEmpty()){
                return true;
            } else {
                channel.sendMessage(missingPermsMessage(missingPerms)).queue();
            }
        }
        return false;
    }

    /**
     * Check if self member has the specified permissions in the provided guild
     * @param guild Guild to check self member for
     * @param channel Channel to send error in and to check the permission for
     * @param perms Perms to check for
     * @return True if we have the permissions we need
     */
    public static boolean hasPermissions(Guild guild, TextChannel channel, NeededPermissions.channelPerms perms) {
        Member member = guild.getSelfMember();

        if (member.hasPermission(Permission.ADMINISTRATOR)){
            return true;
        } else {
            ArrayList<Permission> missingPerms = new ArrayList<>();
            for (Permission permission : perms.getPermissions()){
                if (!member.hasPermission(channel, permission)){
                    missingPerms.add(permission);
                }
            }
            if (missingPerms.isEmpty()){
                return true;
            } else {
                channel.sendMessage(missingPermsMessage(missingPerms)).queue();
            }
        }
        return false;
    }

    /**
     * Create missing permission message
     * @param missingPerms Permission that are missing
     * @return A message explaining what permission are missing
     */
    private static MessageEmbed missingPermsMessage(ArrayList<Permission> missingPerms) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle("Missing permissions!");
        embedBuilder.setDescription("Sorry I can't perform that command here because I'm missing the following permissions:\n");

        for (Permission permission : missingPerms){
            embedBuilder.appendDescription(permission.getName() + "\n");
        }

        return embedBuilder.build();
    }
}
