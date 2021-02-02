package com.teri.alttd.Listeners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class GuildMessageReceivedListener extends ListenerAdapter {

    HashMap adminGroups = new HashMap<Long, ArrayList<Long>>(); //All admin groups for all guilds that have added admin groups.

    public GuildMessageReceivedListener(){
        //TODO on creation load in all data needed for receiving messages.
        // This will likely be:
        // - A list of all guilds along with the groups that should have admin permissions
        // - A list of all guilds along with the GroupTimer objects (name might change)
    }

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event){
        //TODO handle commands.
    }
}
