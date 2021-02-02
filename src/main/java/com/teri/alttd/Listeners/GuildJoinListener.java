package com.teri.alttd.Listeners;

import com.teri.alttd.Queries.GuildJoinQuery;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildJoinListener extends ListenerAdapter {

    public void onGuildJoin(@NotNull GuildJoinEvent event){
        GuildJoinQuery.run(event.getGuild().getIdLong()); //Store the guild that we joined in the database.
        //TODO send some kind of hi message maybe? Could be in a channel or in dms to the server owner
    }

}
