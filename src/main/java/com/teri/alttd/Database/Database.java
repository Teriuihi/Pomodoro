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
        } catch (SQLException e) {
            new Log(Log.LogType.SQL).appendLog(e.getStackTrace());
            e.printStackTrace();
        }

    }

    /**
     * Create guild table if it doesn't exist.
     * This table holds all the guild id's.
     * @throws SQLException
     */
    private static void createGuildTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS guilds" +
                "(guild_id BIGINT NOT NULL," +
                "prefix CHAR(1) DEFAULT '!'," +
                "PRIMARY KEY (guild_id))";

        connection.prepareStatement(query).execute();
    }
}
