package com.teri.alttd.Queries;

import com.teri.alttd.Database.Database;
import com.teri.alttd.FileManagement.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuildQueries {
    static final String guildLeave = "DELETE FROM guilds WHERE guild_id = ?";
    static final String guildJoin = "INSERT INTO guilds VALUES (?)";
    static final String getAllGuilds = "SELECT guild_id FROM guilds";

    //Get guilds -------------------------------------------------------------------------------------------------------

    /**
     * Get all guild ids stored in the database.
     */
    public static ArrayList<Long> getAllGuilds(){

        ArrayList<Long> guildIds = new ArrayList<>();

        try {
            PreparedStatement statement = Database.connection.prepareStatement(getAllGuilds);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                guildIds.add(resultSet.getLong("guild_id"));
            }

        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }

        return guildIds;
    }

    //Join guilds ------------------------------------------------------------------------------------------------------

    /**
     * Store the guild we joined in the database.
     * @param guildId Guild id to store.
     */
    public static void guildJoin(long guildId){
        try {
            PreparedStatement statement = Database.connection.prepareStatement(guildJoin);

            statement.setLong(1, guildId);

            statement.execute();
        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
    }

    /**
     * Add the guilds that were specified in the database in a batch update.
     * @param guildIds List of guild ids to add.
     */
    public static void guildJoin(ArrayList<Long> guildIds) {
        if (guildIds.isEmpty()){
            return;
        }

        try {
            PreparedStatement statement = Database.connection.prepareStatement(guildJoin);

            for (Long guildId : guildIds) {
                statement.setLong(1, guildId);
                statement.addBatch();
            }

            statement.executeBatch();

        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
    }

    //Leave guilds -----------------------------------------------------------------------------------------------------

    /**
     * Remove the guild we joined from the database.
     * @param guildId Guild id to remove.
     */
    public static void guildLeave(long guildId){
        try {
            PreparedStatement statement = Database.connection.prepareStatement(guildLeave);

            statement.setLong(1, guildId);

            statement.execute();
        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
    }

    /**
     * Remove the guilds that were specified from the database in a batch update.
     * @param guildIds List of guild ids to remove.
     */
    public static void guildLeave(ArrayList<Long> guildIds) {
        if (guildIds.isEmpty()){
            return;
        }

        try {
            PreparedStatement statement = Database.connection.prepareStatement(guildLeave);

            for (Long guildId : guildIds) {
                statement.setLong(1, guildId);
                statement.addBatch();
            }

            statement.executeBatch();

        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
}
