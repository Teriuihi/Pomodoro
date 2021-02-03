package com.teri.alttd.Queries;

import com.teri.alttd.Database.Database;
import com.teri.alttd.FileManagement.Log;
import com.teri.alttd.Objects.Pom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PomQueries {
    private static final String pomAdd = "INSERT INTO active_poms (owner_id, session_length, break_length, cycles_amount, " +
            "guild_id, channel_id, role_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String pomDelete = "DELETE FROM active_poms WHERE pom_id = ?";
    private static final String pomLoad = "SELECT * FROM active_poms";
    private static final String getPomIdQuery = "SELECT pom_id FROM active_poms WHERE owner_id = ? AND guild_id = ? AND channel_id = ? AND role_id = ?";
    private static final String specialDelete = "DELETE FROM active_poms WHERE owner_id = ? AND guild_id = ? AND channel_id = ? AND role_id = ?";

    /**
     * Adds a new pom to the database
     * @param workTime Length of work session
     * @param breakTime Length of break
     * @param cycles Amount of cycles
     * @param ownerId Id of the user who started the pom
     */
    public static void addPom(long ownerId, int workTime, int breakTime, int cycles, long guildId, long channelId, long roleId){
        try {
            PreparedStatement statement = Database.connection.prepareStatement(pomAdd);

            statement.setLong(1, ownerId);
            statement.setInt(2, workTime);
            statement.setInt(3, breakTime);
            statement.setInt(4, cycles);
            statement.setLong(5, guildId);
            statement.setLong(6, channelId);
            statement.setLong(7, roleId);

            statement.execute();
        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
    }

    /**
     * Delete a pom from the database
     * @param guildId the guild to delete the pom from
     */
    public static void deletePom(long guildId) {
        try {
            PreparedStatement statement = Database.connection.prepareStatement(pomDelete);

            statement.setLong(1, guildId);

            statement.execute();
        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
    }

    public static int getPomId(long ownerId, long guildId, long channelId, long roleId){
        try {
            PreparedStatement statement = Database.connection.prepareStatement(getPomIdQuery);

            statement.setLong(1, ownerId);
            statement.setLong(2, guildId);
            statement.setLong(3, channelId);
            statement.setLong(4, roleId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("pom_id");
            }

        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Load all poms and put them in a HashMap
     * @return HashMap containing all poms mapped to their guilds
     */
    public static Map<Integer, Pom> loadAllPoms() {
        Map<Integer, Pom> poms = new HashMap<>();

        try {
            PreparedStatement statement = Database.connection.prepareStatement(pomLoad);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                int pomId = resultSet.getInt("pom_id");
                long ownerId = resultSet.getLong("owner_id");
                int workTime = resultSet.getInt("session_length");
                int breakTime = resultSet.getInt("break_length");
                int cycles = resultSet.getInt("cycles_amount");
                long guildId = resultSet.getLong("guild_id");
                long channelId = resultSet.getLong("channel_id");
                long roleId = resultSet.getLong("role_id");
                poms.put(pomId, new Pom(pomId, ownerId, workTime, breakTime, cycles, guildId, channelId, roleId, true));
            }
        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }

        return poms;
    }

    /**
     * Runs a query to delete an entry if you don't know the pom id, be careful ths could technically delete multiple entries.
     * @param ownerId Id of the user who created the entry.
     * @param guildId Id of the guild it was created in.
     * @param channelId Id of the channel it was created for.
     * @param roleId Id of the role that was created for it.
     */
    public static void specialDelete(long ownerId, long guildId, long channelId, long roleId) {
        try {
            PreparedStatement statement = Database.connection.prepareStatement(specialDelete);

            statement.setLong(1, ownerId);
            statement.setLong(2, guildId);
            statement.setLong(3, channelId);
            statement.setLong(4, roleId);

            statement.execute();
        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
    }
}
