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
            return; //If there is no member in the event, or the message does not start with the correct prefix we can exit
        }

        String[] args = Arrays.copyOfRange(split, 1, split.length); //Command arguments (without the command)
        String command = split[0]; //The command

        if (command.length() < 1){
            return; //If the command is only one character long it can't be a command
        }

        char prefix = command.charAt(0); //The prefix is the first character of the command
        command = command.substring(1); //The rest of the string is the command

        switch (command){ //Check what command was send and send relevant data where it needs to go to execute the command
            case "study":
                new Study(command, args, event).runCommand(prefix);
                break;
            case "help":
                event.getChannel().sendMessage(HelpMessageBuilder.buildMessage(prefix)).queue();
                break;
        }
    }

    /**
     * Checks if the prefix is correct, if it isn't it checks if the bot got tagged by the server owner in case they want to change the prefix
     * @param message The message that got send
     * @param isOwner If the user who send the message is the owner or not
     * @param args The message split by spaces
     * @return true if the correct prefix was used, false in every other case
     */
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
