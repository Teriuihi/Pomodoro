package com.teri.alttd.Scheduling;

import com.teri.alttd.Cache.PomGroups;
import com.teri.alttd.Main;
import com.teri.alttd.Objects.Pom;
import com.teri.alttd.Utilities.Utils;
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
        int pomId = jobExecutionContext.getMergedJobDataMap().getInt("pomId");
        Pom pom = PomGroups.getPom(pomId);
        if (Utils.isNull(this, pom, "getting pom: " + pomId)) return;
        Guild guild = Main.jda.getGuildById(pom.getGuildId());
        if (Utils.isNull(this, guild, "getting guild: " + pom.getGuildId())) return;

        TextChannel channel = guild.getTextChannelById(pom.getChannelId());
        if (Utils.isNull(this, channel, "getting textChannel" + pom.getChannelId())) return;

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
        Utils.isNull(this, member, "retrieving member");
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
}
