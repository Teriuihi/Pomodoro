package com.teri.alttd.Listeners;

import com.teri.alttd.Cache.Prefixes;
import com.teri.alttd.Commands.HelpMessageBuilder;
import com.teri.alttd.Commands.Study;
import com.teri.alttd.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;

public class GuildMessageReceivedListener extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event){

        String[] split = event.getMessage().getContentRaw().toLowerCase().split(" ");

        if (event.getMember() == null || !correctPrefix(event.getMessage(), event.getMember().isOwner(), split)){
            return;
        }

        String[] args = Arrays.copyOfRange(split, 1, split.length);
        String command = split[0];

        if (command.length() < 1){
            return;
        }

        char prefix = command.charAt(0);
        command = command.substring(1);

        switch (command){
            case "study":
                new Study(command, args, event).runCommand(prefix);
                break;
            case "help":
                event.getChannel().sendMessage(HelpMessageBuilder.buildMessage(prefix)).queue();
        }
    }

    private boolean correctPrefix(Message message, boolean isOwner, String[] args) {
        String contentRaw = message.getContentRaw();

        if (contentRaw.length() > 0) {
            boolean equals = Prefixes.getPrefix(message.getGuild().getIdLong()) == contentRaw.charAt(0);

            if (equals){
                return true;
            } else if (isOwner && message.getMentionedUsers().size() != 0){
                //If the owner sends a message without a prefix check if they tagged us
                User user = message.getMentionedUsers().get(0);
                //If they did, check if they are trying to set a prefix\
                if (user.getIdLong() == Main.jda.getSelfUser().getIdLong()
                        && args.length == 3
                        && args[1].equalsIgnoreCase("prefix")){
                    //If they did, change the prefix
                    char prefix = args[2].charAt(0);
                    Prefixes.setGuildPrefix(message.getGuild().getIdLong(), args[2].charAt(0));
                    message.getChannel().sendMessage("The new prefix is `" + prefix + "`!").queue();
                }
            }
        }
        return false;
    }
}
