package com.teri.alttd.Commands;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Study {
    String command;
    String[] args;
    GuildMessageReceivedEvent event;

    /**
     * Initialize the Study command, store the command, arguments, and the event.
     * @param command Command that was executed.
     * @param args Arguments that were given with that command.
     * @param event The event that started the command.
     */
    public Study(String command, String[] args, GuildMessageReceivedEvent event) {
        this.command = command;
        this.args = args;
        this.event = event;
    }

    /**
     * Runs the Study command
     * @param prefix The prefix that was used to start this command
     */
    public void runCommand(char prefix){
        TextChannel channel = event.getChannel();
        if (args.length < 6){
            channel.sendMessage(HelpMessageBuilder.buildMessage(HelpMessageBuilder.HelpType.STUDY, prefix)).queue();
        } else {
            //TODO run command
        }
    }
}
