package com.teri.alttd.Scheduling;

import com.teri.alttd.Cache.PomGroups;
import com.teri.alttd.FileManagement.Log;
import com.teri.alttd.Main;
import com.teri.alttd.Objects.Pom;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class PomJob implements Job {

    /**
     * Run a PomJob
     * @param jobExecutionContext Given context for the job
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Pom pom = PomGroups.getPom(jobExecutionContext.getMergedJobDataMap().getLongValue("pomId"));
        Guild guild = Main.jda.getGuildById(pom.getGuildId());
        if (isNull(guild, "getting guild: " + pom.getGuildId())) return;

        TextChannel channel = guild.getTextChannelById(pom.getChannelId());
        if (isNull(channel, "getting textChannel" + pom.getChannelId())) return;

        Member member = guild.getMemberById(pom.getOwnerId());
        if (member == null){
            guild.retrieveMemberById(pom.getOwnerId()).queue(retrievedMember -> sendMessage(channel, retrievedMember, pom));
        } else {
            sendMessage(channel, member, pom);
        }
    }

    /**
     * Send a message to the group who this pom is for
     * @param channel Channel to send the message in
     * @param member Member who created the pom
     * @param pom The pom we are doing this for
     */
    private void sendMessage(TextChannel channel, Member member, Pom pom){
        isNull(member, "retrieving member");
        channel.sendMessage(pom.getEmbeddedMessage(member, pom.sessionActive())).queue();

        if (pom.getCurrentCycle() >= pom.getCyclesAmount()){
            pom.delete();
        } else {
            if (pom.sessionActive()){
                pom.startBreak();
            } else {
                pom.startSession();
            }
        }
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
