package com.teri.alttd.FileManagement;

import java.util.Properties;

public class Config {
    private final Properties configFile;

    /**
     * Store configFile.
     * @param configFile configFile to use..
     */
    public Config(Properties configFile){
        this.configFile = configFile;
    }

    /**
     * Get the String value from the config file
     * @param key Location of the value
     * @return The value belonging to the key
     */
    public String getProperty(String key){
        try {
            return this.configFile.getProperty(key);
        }
        catch (NullPointerException npe){
            new Log(Log.LogType.ERROR).appendLog(npe.getStackTrace());
            npe.printStackTrace();
        }
        return null;
    }
}
