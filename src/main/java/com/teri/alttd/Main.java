package com.teri.alttd;

import com.teri.alttd.Database.Database;
import com.teri.alttd.FileManagement.Config;
import com.teri.alttd.FileManagement.Log;
import com.teri.alttd.Initiate.ConfigInit;
import com.teri.alttd.Initiate.JDAInit;
import com.teri.alttd.Initiate.UpdateGuildsInDatabase;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.quartz.SchedulerFactory;

import javax.security.auth.login.LoginException;

public class Main {

    public static JDA jda;
    public static Config config;
    public static SchedulerFactory schedFact;

    public static void main(String[] args){
        System.out.println("Starting bot...");
        init();
    }

    /**
     * Start everything needed to run the bot.
     */
    private static void init(){
        config = new Config(new ConfigInit().initializeConfig()); //Load the Config.
        schedFact = new org.quartz.impl.StdSchedulerFactory(); //Start the Quartz Scheduler. //TODO load in relevant data
        Database.initiate(); //Connect to the Database.

        try {
            jda = JDABuilder.createDefault(config.getProperty("JDA.token")).build(); //Start the DiscordBot.
            JDAInit.run(); //Initiate all the listeners
        } catch (LoginException e) {
            new Log(Log.LogType.ERROR).appendLog(e.getStackTrace());
            e.printStackTrace();

            System.out.println("Unable to log in!\nShutting down...");
            new Log(Log.LogType.SHUTDOWN).appendLog("Unable to log in, is the token correct?");

            System.exit(0); //Close the bot because it's not usable if we can't log in
            return;
        }

        new Thread(new UpdateGuildsInDatabase()).start(); //Update the database with any guilds we joined/left while we were down.
    }

}
