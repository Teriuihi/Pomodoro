package com.teri.alttd.Initiate;

import com.teri.alttd.FileManagement.Log;
import com.teri.alttd.Listeners.GuildJoinListener;
import com.teri.alttd.Listeners.GuildLeaveListener;
import com.teri.alttd.Listeners.GuildMessageReceivedListener;
import com.teri.alttd.Main;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JDAInit {

    /**
     * Do everything needed to get the JDA part of the bot started.
     */
    public static void run(){
        if (Main.jda == null){
            System.out.println("JDA is null!\nShutting down...");
            new Log(Log.LogType.SHUTDOWN).appendLog("JDA was null when trying to add EventListeners");

            System.exit(0);//Close the bot because if the jda is somehow null at this point it means we didn't log in and we can't use the bot
            return;
        }

        addEventListeners(new GuildJoinListener(),
                new GuildLeaveListener(),
                new GuildMessageReceivedListener());
    }

    /**
     * Adds all given listeners to JDA.
     * @param listeners listeners to add.
     */
    private static void addEventListeners(ListenerAdapter... listeners) {
        for (ListenerAdapter listener : listeners){
            Main.jda.addEventListener(listener);
        }
    }
}
