package com.teri.alttd.Commands;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Study {
    String command;
    String[] args;
    GuildMessageReceivedEvent event;

    public Study(String command, String[] args, GuildMessageReceivedEvent event) {
        this.command = command;
        this.args = args;
        this.event = event;
    }

    public void runCommand(char prefix){
        TextChannel channel = event.getChannel();
        if (args.length < 6){
            channel.sendMessage(HelpMessageBuilder.buildMessage(HelpMessageBuilder.HelpType.STUDY, prefix)).queue();
        } else {
            //TODO run command
        }
    }
}
