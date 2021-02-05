package com.teri.alttd.Scheduling;

import com.teri.alttd.FileManagement.Log;
import com.teri.alttd.Main;
import com.teri.alttd.Objects.Pom;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;

import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class PomScheduler {

    private final Pom pom;
    private final int pomId;

    public PomScheduler(Pom pom, int pomId) {
        this.pom = pom;
        this.pomId = pomId;
    }

    /**
     * Schedule a new job
     */
    public void schedule(){
        try {
            Scheduler scheduler = Main.schedFact.getScheduler();
            JobDetail jobDetail = getJobDetail(pom.sessionActive());

            scheduler.start();
            Date time;
            if (pom.sessionActive()){
                time = pom.getNextBreak();
            } else {
                time = pom.getNextSession();
            }

            scheduler.scheduleJob(jobDetail, getTrigger(jobDetail, time));
        } catch (SchedulerException e) {
            new Log(Log.LogType.ERROR).appendLog(e.getStackTrace());
            e.printStackTrace();
        }
    }

    /**
     * Create a JobDetail
     * @return the created JobDetail
     */
    private JobDetail getJobDetail(boolean cycle){
        return newJob(PomJob.class).withIdentity(new JobKey(String.valueOf(pomId), "pom_" + (cycle ? "cycle" : "break"))).build();
    }

    /**
     * Create a SimpleTrigger
     * @param job Job to create the trigger with
     * @return the SimpleTrigger after it's created
     */
    private SimpleTrigger getTrigger(JobDetail job, Date time){
        return (SimpleTrigger) newTrigger()
                .withIdentity(String.valueOf(pomId), "pom")
                .startAt(time)
                .forJob(job)
                .usingJobData("pomId", pomId)
                .build();
    }
}
