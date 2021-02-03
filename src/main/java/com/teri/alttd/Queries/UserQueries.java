package com.teri.alttd.Queries;

import com.teri.alttd.Database.Database;
import com.teri.alttd.FileManagement.Log;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserQueries {
    private final static String addUserQuery = "INSERT INTO users (user_id, pom_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE pom_id = ?";
    private final static String deleteUserQuery = "DELETE FROM users WHERE user_id = ?";
    private final static String deleteAllUsersQuery = "DELETE FROM users WHERE pom_id = ?";

    public static void addUser(long userId, int pomId){
        try {
            PreparedStatement statement = Database.connection.prepareStatement(addUserQuery);

            statement.setLong(1, userId);
            statement.setInt(2, pomId);
            statement.setInt(3, pomId);

            statement.execute();
        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
    }

    public static void deleteUser(long userId){
        try {
            PreparedStatement statement = Database.connection.prepareStatement(deleteUserQuery);

            statement.setLong(1, userId);

            statement.execute();
        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
    }

    public static void deleteAllUser(int pomId){
        try {
            PreparedStatement statement = Database.connection.prepareStatement(deleteAllUsersQuery);

            statement.setInt(1, pomId);

            statement.execute();
        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
    }
}
