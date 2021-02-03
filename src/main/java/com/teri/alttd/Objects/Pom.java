package com.teri.alttd.Objects;

import com.teri.alttd.Cache.PomGroups;
import com.teri.alttd.FileManagement.Log;
import com.teri.alttd.Main;
import com.teri.alttd.Queries.PomQueries;
import com.teri.alttd.Queries.UserQueries;
import com.teri.alttd.Scheduling.PomScheduler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Pom {
    private final int pomId;
    private final long ownerId;
    private final int sessionLength;
    private final int breakLength;
    private final int cyclesAmount;
    private final long guildId;
    private final long channelId;
    private final long roleId;
    private Date nextBreak;
    private Date nextSession;
    private int currentCycle = 0;
    private boolean active;
    private boolean sessionActive;
    private ArrayList<Long> users;

    public Pom(int pomId, long ownerId, int sessionLength, int breakLength, int cyclesAmount, long guildId,
               long channelId, long roleId, boolean active) {
        this.pomId = pomId;
        this.ownerId = ownerId;
        this.sessionLength = sessionLength;
        this.breakLength = breakLength;
        this.cyclesAmount = cyclesAmount;
        this.guildId = guildId;
        this.channelId = channelId;
        this.roleId = roleId;
        this.active = active;
        users = new ArrayList<>();
        users.add(ownerId);

        startSession();
    }
    //TODO create constructor to use when starting the pom from database

    public int getPomId() {
        return pomId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public int getSessionLength() {
        return sessionLength;
    }

    public int getBreakLength() {
        return breakLength;
    }

    public int getCyclesAmount() {
        return cyclesAmount;
    }

    public long getGuildId() {
        return guildId;
    }

    public long getChannelId() {
        return channelId;
    }

    public Date getNextBreak() {
        return nextBreak;
    }

    public Date getNextSession() {
        return nextSession;
    }

    public int getCurrentCycle() {
        return currentCycle;
    }

    public boolean isActive() {
        return active;
    }

    public boolean sessionActive() {
        return sessionActive;
    }

    /**
     * Start a break.
     * @return if the break was started successfully.
     */
    public boolean startBreak(){
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MINUTE, breakLength);
        nextSession = new Date(calendar.getTimeInMillis());
        sessionActive = false;

        new PomScheduler(this, pomId).schedule();

        return true;
    }

    /**
     * Start session, if it should have ended already run delete.
     * @return if the session started successfully.
     */
    public boolean startSession(){
        if (currentCycle >= cyclesAmount){
            delete();
            return false;
        }

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MINUTE, sessionLength);
        currentCycle++;
        nextBreak = new Date(calendar.getTimeInMillis());
        sessionActive = true;
        new PomScheduler(this, pomId).schedule();

        return true;
    }

    /**
     * Pause the session.
     * @return if the session paused successfully.
     */
    public boolean pause(){
        if (!active){
            return false;
        } else {
            active = false;
            return true;
        }
    }

    /**
     * Un-pause the session.
     * @return if the session un-paused successfully.
     */
    public boolean unpause(){
        if (active){
            return false;
        } else {
            active = true;
            return true;
        }
    }

    /**
     * Add a user to the user list.
     * @param userId user to add.
     */
    public void addUser(long userId){
        if (!users.contains(userId)){
            users.add(userId);
        }
    }

    /**
     * Delete a user from the user list.
     * @param userId user to delete.
     */
    public void delUser(long userId){
        users.remove(userId);
    }

    /**
     * If a user is in the user list
     * @param userId user to check for
     * @return if the user is in the user list
     */
    public boolean hasUser(long userId) {
        return users.contains(userId);
    }

    /**
     * Create embedded message to send when a session end.
     * @param member Member who created the pom.
     * @param sessionEnd If this is the message to be send after a session is ended, if false it's for after a break
     * @return the requested embedded message.
     */
    public MessageEmbed getEmbeddedMessage(Member member, boolean sessionEnd) {
        EmbedBuilder eb = new EmbedBuilder();
        StringBuilder stringBuilder = new StringBuilder();

        if (sessionEnd) {
            stringBuilder.append("Session ").append(currentCycle).append(" ended! ");
            if (cyclesAmount - currentCycle == 0) {
                stringBuilder.append("This was the final session, good job!");
            } else {
                stringBuilder.append("There are ").append(cyclesAmount - currentCycle)
                        .append(" ").append(sessionLength)
                        .append(" minutes sessions left after this one, but for now take a ")
                        .append(breakLength).append(" minute break! ")
                        .append("I will notify you when the next session starts!");
            }
        } else {
            stringBuilder.append("Break ended! Starting session ").append(currentCycle+1)
                    .append(" out of ").append(cyclesAmount).append(" now.");
            if (currentCycle+1 >= cyclesAmount){
                stringBuilder.append(" This will be the final session!");
            }
        }

        String nickname;
        if (member == null){
            nickname = "invalid member";
        } else {
            nickname = member.getNickname();
        }

        eb.setTitle(nickname + "'s pomodoro");
        eb.setColor(Color.BLUE);
        eb.setDescription(stringBuilder.toString());

        return eb.build();
    }

    /**
     * Delete the pom from the database.
     * Delete the pom role.
     * Delete the scheduled job if it exists.
     * Remove pom from the PomGroups
     */
    public void delete() {
        PomQueries.deletePom(guildId);
        deleteRole();
        try {
            Main.schedFact.getScheduler().deleteJob(new JobKey(String.valueOf(pomId), "pom"));
        } catch (SchedulerException e) {
            new Log(Log.LogType.ERROR).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
        PomGroups.removePom(pomId);
        UserQueries.deleteAllUser(pomId);
    }

    /**
     * Delete the role that was made for this pom from discord
     */
    private void deleteRole() {
        Guild guild = Main.jda.getGuildById(guildId);
        if (isNull(guild, "getting guild " + guildId + " to delete role")) return;
        Role role = guild.getRoleById(roleId);
        if (isNull(role, "getting role " + roleId + " to delete the role")) return;
        role.delete().queue();
    }

    /**
     * Check if an object is null and if it is log it
     * @param object Object to check
     * @param action Action that was taken that should have resulted in the object not being null
     * @return true if the object is null
     */
    private boolean isNull(Object object, String action){
        if (object == null) {
            new Log(Log.LogType.NULL).appendLog("Unexpected null at " + this.getClass().getName() + " during the following action: " + action);
            return true;
        }
        return false;
    }
}
