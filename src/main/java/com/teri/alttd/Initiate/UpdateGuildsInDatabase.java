package com.teri.alttd.Initiate;

import com.teri.alttd.Main;
import com.teri.alttd.Queries.GetGuildsQuery;
import com.teri.alttd.Queries.GuildJoinQuery;
import com.teri.alttd.Queries.GuildLeaveQuery;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;

public class UpdateGuildsInDatabase {

    /**
     * Go through all cached guilds and compare them to the guilds stored in the database.
     * If there are any guilds that we're not in store them in a list.
     * If there are any guilds that are not cached but are in the db leave them in the list we got form the db, remove the rest.
     * Remove the guilds we left from the db, add the guilds we joined to the db.
     */
    public static void run(){
        ArrayList<Long> guildIds = GetGuildsQuery.run();
        ArrayList<Long> joinedGuildIds = new ArrayList<>();

        for (Guild guild : Main.jda.getGuilds()){
            long guildId = guild.getIdLong();
            if (guildIds.contains(guildId)){
                guildIds.remove(guildId); //We're still in here so remove it from the list.
            } else {
                joinedGuildIds.add(guildId); //We joined this guild so add it to the joined guilds list.
            }
        }

        GuildLeaveQuery.run(guildIds); //Remove all guilds that weren't cached but were in the database.
        GuildJoinQuery.run(joinedGuildIds); //Add all guilds that were in the cache but not in the database.
    }

}
