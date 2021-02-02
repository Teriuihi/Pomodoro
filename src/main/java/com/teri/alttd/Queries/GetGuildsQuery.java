package com.teri.alttd.Queries;

import com.teri.alttd.Database.Database;
import com.teri.alttd.FileManagement.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetGuildsQuery {
    static final String query = "SELECT guild_id FROM guilds";

    /**
     * Get all guild ids stored in the database.
     */
    public static ArrayList<Long> run(){

        ArrayList<Long> guildIds = new ArrayList<>();

        try {
            PreparedStatement statement = Database.connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                guildIds.add(resultSet.getLong("guild_id"));
            }

        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
        }

        return guildIds;
    }
}
