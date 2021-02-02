package com.teri.alttd.Queries;

import com.teri.alttd.Database.Database;
import com.teri.alttd.FileManagement.Log;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuildJoinQuery {

    static final String query = "INSERT INTO guilds VALUES (?)";

    /**
     * Store the guild we joined in the database.
     * @param guildId Guild id to store.
     */
    public static void run(long guildId){
        try {
            PreparedStatement statement = Database.connection.prepareStatement(query);

            statement.setLong(1, guildId);

            statement.execute();
        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
        }
    }

    /**
     * Add the guilds that were specified in the database in a batch update.
     * @param guildIds List of guild ids to add.
     */
    public static void run(ArrayList<Long> guildIds) {
        try {
            PreparedStatement statement = Database.connection.prepareStatement(query);

            for (Long guildId : guildIds) {
                statement.setLong(1, guildId);
                statement.addBatch();
            }

            statement.executeBatch();

        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
        }
    }
}
