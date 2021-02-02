package com.teri.alttd.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;

public class HelpMessageBuilder {

    private static final String title = "Pomodoro help menu";

    /**
     * Enum containing a help message for every command
     */
    public enum HelpType{
        STUDY("Study Command",
                "Command description - Schedule a time to study, keep in mind we use a 24 hour clock and time has to be specified in UTC/GMT.\n" +
                        "Command usage - `{0}study schedule <dd-mm-yyyy> <hh-mm> Type: <session type> Topic: <session topic>`\n" +
                        "Example usage - `{0}study schedule 22-02-2021 16:00 Type: Public Topic: Programming in Java` This would " +
                        "start a study session at February 22nd 2021 at 4 PM UTC, everyone will be able to join it and the topic " +
                        "would be Programming in Java."),
        POM_START("Pom Start Command",
                "Command description - Start a pomodoro session, keep in mind time is in minutes." +
                        "Command usage - `{0}pom start <work time> <break time> <number of cycles>`\n" +
                        "Example usage - `{0}pom start 20 5 4` This would start a 20 minute work session with 5 minute breaks, 4 times."),
        POM_PAUSE("Pom Pause Command",
                          "Command description - Pause a pomodoro session." +
                          "Command usage - `{0}pom pause` Pauses a pomodoro session until it is continued with `{0}pom continue`."),
        POM_STOP("Pom Stop Command",
                          "Command description - Stops a pomodoro session." +
                          "Command usage - `{0}pom stop` Stops a pomodoro session and cancels it.");

        private final String helpTitle;
        private final String helpMessage;

        HelpType(String helpTitle, String helpTest) {
            this.helpTitle = helpTitle;
            this.helpMessage = helpTest;
        }
    }

    /**
     * Build the help message for the specified HelpType.
     * @param helpType the help type to build the help message for.
     * @param prefix the prefix the server that the message is for is using.
     * @return The help message for the specified HelpType.
     */
    public static MessageEmbed buildMessage(HelpType helpType, char prefix){
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(Color.magenta);
        embedBuilder.setTitle(title);
        embedBuilder.addField(helpType.helpTitle, String.format(helpType.helpMessage, prefix), false);

        return embedBuilder.build();
    }

    /**
     * Creates the help message for every command.
     * @param prefix the prefix the server that the message is for is using.
     * @return The full help message.
     */
    public static MessageEmbed buildMessage(char prefix){
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(Color.magenta);
        embedBuilder.setTitle(title);

        for (HelpType helpType : HelpType.values()){
            embedBuilder.addField(helpType.helpTitle, String.format(helpType.helpMessage, prefix), false);
        }

        return embedBuilder.build();
    }

}
