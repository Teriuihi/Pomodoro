package com.teri.alttd.Database;

import com.teri.alttd.FileManagement.Config;
import com.teri.alttd.FileManagement.Log;
import com.teri.alttd.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static Connection connection = null;

    /**
     * Initiates the database by setting the connection and adding any missing tables.
     */
    public static void initiate(){

        String ip, port, name, username, password;
        Config config = Main.config;

        ip = config.getProperty("Database.ip");
        port = config.getProperty("Database.port");
        name = config.getProperty("Database.name");
        username = config.getProperty("Database.username");
        password = config.getProperty("Database.password");

        String url = "jdbc:mysql://" + ip + ":" + port + "/" + name;

        try{
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();

            System.out.println("Unable to connect to database!\nShutting down...");
            new Log(Log.LogType.SHUTDOWN).appendLog("Unable to connect to database!");

            System.exit(0); //Close the bot because without a database connection it can't run.
            return;
        }

        try {
            createGuildTable();
            createPomTable();
            createUsersTable();
        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }

    }

    /**
     * Create guild table if it doesn't exist.
     * This table holds all the guild id's.
     * @throws SQLException on query error or connection error
     */
    private static void createGuildTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS guilds" +
                "(guild_id BIGINT NOT NULL, " +
                "prefix CHAR(1) DEFAULT '!', " +
                "PRIMARY KEY (guild_id))";

        connection.prepareStatement(query).execute();
    }

    private static void createPomTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS active_poms" +
                "(pom_id INT NOT NULL AUTO_INCREMENT, " +
                "owner_id BIGINT NOT NULL, " +
                "session_length INT NOT NULL, " +
                "break_length INT NOT NULL, " +
                "cycles_amount INT NOT NULL, " +
                "guild_id BIGINT NOT NULL, " +
                "channel_id BIGINT NOT NULL, " +
                "role_id BIGINT NOT NULL, " +
                "PRIMARY KEY (pom_id))";

        connection.prepareStatement(query).execute();
    }

    private static void createUsersTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS users" +
                "(user_id BIGINT NOT NULL, " +
                "pom_id INT NOT NULL, " +
                "PRIMARY KEY (user_id)," +
                "FOREIGN KEY (pom_id) " +
                "REFERENCES active_poms(pom_id) " +
                "ON DELETE CASCADE ON UPDATE CASCADE)";

        connection.prepareStatement(query).execute();
    }
}
