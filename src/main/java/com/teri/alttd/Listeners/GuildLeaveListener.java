package com.teri.alttd.Listeners;

import com.teri.alttd.Queries.GuildQueries;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildLeaveListener extends ListenerAdapter {

    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        GuildQueries.guildLeave(event.getGuild().getIdLong()); //Remove the entry for this guild from the database.
    }

}
