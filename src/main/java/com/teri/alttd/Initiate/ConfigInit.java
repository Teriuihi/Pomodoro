package com.teri.alttd.Initiate;

import com.teri.alttd.FileManagement.Config;
import com.teri.alttd.FileManagement.CreateConfig;
import com.teri.alttd.FileManagement.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigInit {

    private Properties configFile;

    /**
     * Create the config file if it doesn't exist and load any missing data into it.
     */
    public Properties initializeConfig(){
        if (!configExists()){
            touchConfig();
        }
        loadConfig();
        CreateConfig conf = new CreateConfig();
        conf.initConfig(configFile, getStringPath());
        return configFile;
    }

    /**
     * Load the existing config file
     */
    private void loadConfig()
    {
        configFile = new java.util.Properties();
        try {
            InputStream input = new FileInputStream(getStringPath());
            configFile.load(input);
        } catch (IOException e) {
            new Log(Log.LogType.ERROR).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
    }

    /**
     * Check if the config exists.
     * @return true if the config exists.
     */
    private boolean configExists() {
        return getFilePath().exists();
    }

    /**
     * Get the path of the config.
     * @return the String value of the config path.
     */
    private String getStringPath(){
        Path currentDir = null;
        try {
            currentDir = Paths.get(new File(Config.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
            currentDir = currentDir.getParent();
        } catch (URISyntaxException e) {
            new Log(Log.LogType.ERROR).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
        return currentDir.toString() + "//properties//config.properties";
    }

    /**
     * Get the config file path.
     * @return the File for the config.
     */
    private File getFilePath(){
        return new File(getStringPath());
    }

    /**
     * Create the config file.
     */
    private void touchConfig() {
        try {
            new File(getStringPath()).createNewFile();
        } catch (IOException e) {
            new Log(Log.LogType.ERROR).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
    }
}
