package com.teri.alttd.Queries;

import com.teri.alttd.Database.Database;
import com.teri.alttd.FileManagement.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class GuildQueries {
    private static final String guildLeave = "DELETE FROM guilds WHERE guild_id = ?";
    private static final String guildJoin = "INSERT INTO guilds (guild_id) VALUES (?)";
    private static final String getAllGuilds = "SELECT guild_id FROM guilds";
    private static final String getAllGuildPrefixes = "SELECT guild_id, prefix FROM guilds";
    private static final String guildUpdate = "INSERT INTO guilds (guild_id, prefix) VALUES(?, ?) ON DUPLICATE KEY UPDATE prefix = ?";

    //Get guilds -------------------------------------------------------------------------------------------------------

    /**
     * Get all guild ids stored in the database.
     * @return all guild id's in an ArrayList
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

    /**
     * Get all guild prefixes mapped to their guild id
     * @return All guild prefixes in a HashMap mapped to their guildId
     */
    public static HashMap<Long, Character> getAllGuildPrefixes(){

        HashMap<Long, Character> guilds = new HashMap<>();

        try {
            PreparedStatement statement = Database.connection.prepareStatement(getAllGuildPrefixes);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                guilds.put(resultSet.getLong("guild_id"), resultSet.getString("prefix").charAt(0));
            }

        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }

        return guilds;
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

    //Update guilds ----------------------------------------------------------------------------------------------------

    /**
     * Updates the entry for this guild (and if it doesn't exist it creates it)
     * @param guildId The id of the guild to update
     * @param prefix The new prefix
     */
    public static void guildUpdate(long guildId, char prefix) {
        try {
            PreparedStatement statement = Database.connection.prepareStatement(guildUpdate);

            statement.setLong(1, guildId);
            statement.setString(2, String.valueOf(prefix));
            statement.setString(3, String.valueOf(prefix));

            statement.executeUpdate();

        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------------------

}
