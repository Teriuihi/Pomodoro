package com.teri.alttd.FileManagement;

import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class CreateConfig {

    /**
     * All the settings that should be inserted into the config.
     */
    private enum Settings {

        ip("Database.ip", "localhost"),
        port("Database.port", "3306"),
        name("Database.name", "Pomodoro"),
        username("Database.username", "pomodoro"),
        password("Database.password", "root"),
        token("JDA.token", "undefined");

        private final String key;
        private final String value;

        Settings(String key, String value){
            this.key = key;
            this.value = value;
        }
    }

    /**
     * Load any missing value's into the config.
     * @param config Config to load value's into.
     * @param configPath Path the config is stored at.
     */
    public void initConfig(@NotNull Properties config, @NotNull String configPath) {
        for (Settings settings : Settings.values()) {
            if (!config.containsKey(settings.key)) {
                config.setProperty(settings.key, settings.value);
                addedToConfig(settings.key);
            }
        }
        try {
            config.store(new FileOutputStream(configPath), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to send message to console that key was added to config.
     * @param key Key that was added to the config.
     */
    private void addedToConfig(String key) {
        System.out.println("Added " + key + " to config!");
    }

}
