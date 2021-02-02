package com.teri.alttd.Cache;

import com.teri.alttd.Queries.GuildQueries;

import java.util.HashMap;

public class Prefixes {
    private static final HashMap<Long, Character> guilds = new HashMap<>();

    /**
     * Get the prefix for the guild who's id is provided
     * @param guildId guild to get the prefix for
     * @return the prefix for the provided guild id
     */
    public static char getPrefix(long guildId){
        return guilds.get(guildId);
    }

    /**
     * Set the guild id and update it in the database
     * @param guildId guild to get the prefix for
     * @param prefix value to change the prefix to
     */
    public static void setGuildPrefix(long guildId, char prefix){
        guilds.put(guildId, prefix);
        GuildQueries.guildUpdate(guildId, prefix);
    }

    /**
     * Load all prefixes from database
     */
    public static void loadGuildPrefixes(){
        guilds.putAll(GuildQueries.getAllGuildPrefixes());
    }
}
