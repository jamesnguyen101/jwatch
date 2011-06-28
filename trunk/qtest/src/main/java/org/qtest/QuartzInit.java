/**
 * JWatch - Quartz Monitor: http://code.google.com/p/jwatch/
 * Copyright (C) 2011 Roy Russo and the original author or authors.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA 02110-1301 USA
 **/

package org.qtest;

import org.apache.log4j.Logger;
import org.qtest.job.HelloJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: 6/28/11 1:51 PM
 */
public class QuartzInit implements ServletContextListener {
    private Scheduler sched = null;
    private Scheduler sched3 = null;
    private Scheduler sched2 = null;
    static Logger log = Logger.getLogger(QuartzInit.class);

    public void contextInitialized(ServletContextEvent event) {
        try {
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

            sched = schedFact.getScheduler();

            sched.start();

            // define the job and tie it to our HelloJob class
            JobDetail job = newJob(HelloJob.class)
                    .withIdentity("myJob", "group1").withDescription("Some rand job").storeDurably(true)
                    .build();

            // Trigger the job to run now, and then every 40 seconds
            Trigger trigger = newTrigger()
                    .withIdentity("myTrigger", "group1")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(120)
                            .repeatForever())
                    .build();

            // Tell quartz to schedule the job using our trigger
            sched.scheduleJob(job, trigger);
            log.info("Job Created: " + job);

            // Job #2
            // define the job and tie it to our HelloJob class
            job = newJob(HelloJob.class)
                    .withIdentity("myJob2", "group1").withDescription("Some rand job2").storeDurably(true)
                    .build();

            // Trigger the job to run now, and then every 40 seconds
            trigger = newTrigger()
                    .withIdentity("myTrigger2", "group1")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInMinutes(5)
                            .repeatForever())
                    .build();

            // Tell quartz to schedule the job using our trigger
            sched.scheduleJob(job, trigger);
            log.info("Job Created: " + job);

            // Group #2, Job #1
            // define the job and tie it to our HelloJob class
            job = newJob(HelloJob.class)
                    .withIdentity("myJob1", "group2").withDescription("Some rand job3").storeDurably(true)
                    .build();

            // Trigger the job to run now, and then every 40 seconds
            trigger = newTrigger()
                    .withIdentity("myTrigger3", "group2")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInMinutes(1)
                            .repeatForever())
                    .build();

            // Tell quartz to schedule the job using our trigger
            sched.scheduleJob(job, trigger);
            log.info("Job Created: " + job);

            // second scheduler
            // Tell Quartz to look out out for quartz2.properties
            System.setProperty(StdSchedulerFactory.PROPERTIES_FILE, "quartz2.properties");
            schedFact = new org.quartz.impl.StdSchedulerFactory();
            sched2 = schedFact.getScheduler();
            sched2.start();
            // define the job and tie it to our HelloJob class
            JobDetail jobx = newJob(HelloJob.class)
                    .withIdentity("myJobx", "groupx").withDescription("Some rand jobx").storeDurably(true)
                    .build();
            // Trigger the job to run now, and then every 40 seconds
            Trigger triggerx = newTrigger().forJob(jobx)
                    .withIdentity("myTriggerx", "groupx")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(450)
                            .repeatForever())
                    .build();
            // Tell quartz to schedule the job using our trigger
            sched2.addJob(jobx, true);
            sched2.scheduleJob(triggerx);
            log.info("Job Created: " + jobx);
            Trigger triggerx2 = newTrigger().forJob(jobx)
                    .withIdentity("myTriggerx2", "groupx2")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInHours(1)
                            .repeatForever())
                    .build();
            // Tell quartz to schedule the job using our trigger
            sched2.scheduleJob(triggerx2);

            System.setProperty(StdSchedulerFactory.PROPERTIES_FILE, "quartz3.properties");
            schedFact = new org.quartz.impl.StdSchedulerFactory();
            sched3 = schedFact.getScheduler();
            sched3.start();
            int groups = 2;
            int jobspergroup = 2;
            for (int g = 0; g < groups; g++) {
                String group = "group" + g;
                for (int i = 0; i < jobspergroup; i++) {
                    JobDetail jd1 = newJob(HelloJob.class)
                            .withIdentity("j_" + i, group).withDescription("Some rand jobx").storeDurably(true)
                            .build();
                    //new JobDetail("j_" + i, group, HelloJob.class);

                    Trigger tr1 = newTrigger()
                            .withIdentity("t_" + i, group)
                            .startNow()
                            .withSchedule(cronSchedule(String.valueOf(i % 60) + " * * * * ?"))
                            .build();

                    //new CronTrigger("t_" + i, group, String.valueOf(i % 60) + " * * * * ?");
                    sched3.scheduleJob(jd1, tr1);
                }
            }
            log.info("Finished job loop");
        } catch (Throwable se) {
            se.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent event) {
        try {
            sched.shutdown();
            sched2.shutdown();
            sched3.shutdown();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
