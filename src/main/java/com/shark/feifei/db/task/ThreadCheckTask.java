package com.shark.feifei.db.task;

import com.shark.feifei.annoation.TaskClose;
import com.shark.feifei.data.KeyString;
import com.shark.feifei.db.FeifeiPoolDatasource;
import com.shark.job.job.AbstractScheduleJob;
import org.quartz.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Check thread is alive that get connection
 * @Author: Shark Chili
 * @Date: 2018/11/19 0019
 */
@TaskClose
public class ThreadCheckTask extends AbstractScheduleJob {

	public ThreadCheckTask() {
		//初始化 trigger,jobDetail.
		setJOB_KEY(JobKey.jobKey(this.getClass().getName(), "initTaskJobGroup"));
		setTRIGGER_KEY(TriggerKey.triggerKey(this.getClass().getName(), "initTaskJobGroup"));
		setJobDetail(JobBuilder.newJob(ThreadCheckTask.class).withIdentity(getJobKey()).build());
		setTrigger(TriggerBuilder.newTrigger()
				.withIdentity(getTriggerKey())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds((int) TaskTime.THREAD_CHECK_INTERVAL.getTime())//3秒一次检测连接
						.repeatForever())
				.startAt(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(TaskTime.THREAD_CHECK_DELAY.getTime())))
				.build());
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		FeifeiPoolDatasource datasource= (FeifeiPoolDatasource) context.getJobDetail().getJobDataMap().get(KeyString.DATASOURCE);
		datasource.checkThreadIsAlive();
	}
}