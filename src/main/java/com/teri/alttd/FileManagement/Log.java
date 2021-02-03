package com.teri.alttd.FileManagement;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class Log {
    private final String logName;
    private Path path;

    /**
     * All possible log types and if a date should be used in the log name.
     */
    public enum LogType {
        ERROR("errorLog.txt", true),
        SQL("sqlLog.txt", true),
        SHUTDOWN("shutdown.txt", false),
        NULL("unexpectedNull.txt", true);

        private final String logName;
        private final boolean useDate;

        LogType(String logName, boolean useDate){
            this.logName = logName;
            this.useDate = useDate;
        }
    }

    /**
     * Create a log to add entries to.
     * @param logType Type of log.
     */
    public Log(LogType logType) {
        if (logType.useDate) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
            Date date = new Date();
            this.logName = dateFormat.format(date.getTime()) + "-" + logType.logName;
        } else {
            this.logName = logType.logName;
        }
        path = getPath();
    }

    /**
     * Adds text to log.
     * @param toWrite Text to add to log.
     */
    public void appendLog(String toWrite){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        if (path != null && path.toFile().exists()){
            try {
                Files.write(path, ("["+ dtf.format(now) + "] " + toWrite + "\n").getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Couldn't append error to '" + logName + "' because it doesn't exist");
        }
    }

    /**
     * Adds text to log.
     * @param stackTrace StackTrace to add to log.
     */
    public void appendLog(StackTraceElement[] stackTrace){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        if (path != null && path.toFile().exists()){
            try {
                for (StackTraceElement ste:stackTrace){
                    Files.write(path, ("["+ dtf.format(now) + "] " + ste.toString() + "\n").getBytes(), StandardOpenOption.APPEND);
                }
                Files.write(path, ("\n").getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Couldn't append error to '" + logName + "' because it doesn't exist");
        }
    }

    /**
     * Get the path the log is stored at.
     * @return the path of the log.
     */
    private Path getPath(){
        String dir = getLogDirectory();
        String filePath = dir + File.separator + logName;

        Path path = Paths.get(filePath);
        if (!path.toFile().exists()){
            File file = new File(filePath);
            try {
                if (!file.createNewFile()){
                    System.out.println("Couldn't create " + logName + ".");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (path.toFile().exists()){
            return path;
        } else {
            return null;
        }
    }

    /**
     * Get the directory the log is stored in.
     * @return The path to the directory the log is stored in as a String/
     */
    private String getLogDirectory() {
        Path currentDir;
        Path newDir = null;

        try {
            currentDir = Paths.get(new File(Log.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
            currentDir = currentDir.getParent();
            newDir = Paths.get(currentDir.toString() + File.separator + "logs");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if(!Files.exists(Objects.requireNonNull(newDir))){
            File file = new File(newDir.toString());
            file.mkdir();
        }
        return newDir.toString();
    }
}
