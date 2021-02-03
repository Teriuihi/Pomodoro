package com.teri.alttd.Commands;

import com.teri.alttd.Cache.PomGroups;
import com.teri.alttd.FileManagement.Log;
import com.teri.alttd.Objects.Pom;
import com.teri.alttd.Queries.PomQueries;
import com.teri.alttd.Queries.UserQueries;
import com.teri.alttd.Utilities.HelpMessageBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PomCommand {
    String command;
    String[] args;
    GuildMessageReceivedEvent event;

    /**
     * Initialize the Pom command, store the command, arguments, and the event.
     * @param command Command that was executed.
     * @param args Arguments that were given with that command.
     * @param event The event that started the command.
     */
    public PomCommand(String command, String[] args, GuildMessageReceivedEvent event) {
        this.command = command;
        this.args = args;
        this.event = event;
    }

    /**
     * Runs the Pom command
     * @param prefix The prefix that was used to start this command
     */
    public void runCommand(char prefix){
        if (args.length < 1){
            return;
        }

        switch (args[0]){
            case "start":
                if (args.length == 4){
                    for (String arg : args){
                        if (arg.equals("start")) continue;
                        if (!arg.matches("[1-9][0-9]{0,2}")){
                            event.getChannel().sendMessage(HelpMessageBuilder.buildMessage(prefix, HelpMessageBuilder.HelpType.POM_START)).queue();
                            return;
                        }
                    }
                    event.getGuild().createRole().setName(event.getMember().getNickname()).setMentionable(true)
                            .queue(role -> createPom(event, args, role));
                    return;
                }
                event.getChannel().sendMessage(HelpMessageBuilder.buildMessage(prefix, HelpMessageBuilder.HelpType.POM_START)).queue();
                break;
            case "pause":
                break;
            case "continue":
                break;
            case "stop":
                break;
            case "leave":
                break;
            default:
                event.getChannel().sendMessage(HelpMessageBuilder.buildMessage(prefix,
                                HelpMessageBuilder.HelpType.POM_START,
                                HelpMessageBuilder.HelpType.POM_PAUSE,
                                HelpMessageBuilder.HelpType.POM_STOP)).queue();
        }
    }

    /**
     * Create pom and store it in the database
     * @param event Event that called for the creation
     * @param args Arguments that were provided for the creation
     * @param role Role that was created for the creation
     */
    private void createPom(GuildMessageReceivedEvent event, String[] args, Role role) {
        long ownerId = event.getMember().getIdLong();
        int workTime = Integer.parseInt(args[1]);
        int breakTime = Integer.parseInt(args[2]);
        int cycles = Integer.parseInt(args[3]);
        long guildId = event.getGuild().getIdLong();
        long channelId = event.getChannel().getIdLong();
        long roleId = role.getIdLong();
        PomQueries.addPom(ownerId, workTime, breakTime, cycles, guildId, channelId, roleId);
        int pomId = PomQueries.getPomId(ownerId, guildId, channelId, roleId);

        if (pomId == 0){
            new Log(Log.LogType.NULL).appendLog("Received 0 as id for pom, this shouldn't be possible" +
                    "guild id: " + guildId + " channel id: " + channelId + " role id: " + roleId);
            role.delete().queue();
            event.getChannel().sendMessage("Something went wrong while scheduling this pom!").queue();
            PomQueries.specialDelete(ownerId, guildId, channelId, roleId);
            return;
        }

        Pom pom = new Pom(pomId, ownerId, workTime, breakTime, cycles, guildId, channelId, roleId, true);
        PomGroups.addPom(pomId, pom);
        UserQueries.addUser(ownerId, pomId);
    }
}
