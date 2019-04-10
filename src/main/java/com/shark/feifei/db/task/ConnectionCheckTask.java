package com.shark.feifei.db.task;

import com.shark.feifei.annoation.TaskClose;
import com.shark.feifei.data.KeyString;
import com.shark.feifei.db.FeifeiPoolDatasource;
import com.shark.job.job.AbstractScheduleJob;
import org.quartz.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Check database connection whether is valid or not
 * @Author: Shark Chili
 * @Date: 2018/9/5 0005
 */
@TaskClose
public class ConnectionCheckTask extends AbstractScheduleJob {

	public ConnectionCheckTask() {
		//初始化 trigger,jobDetail.
		setJOB_KEY(JobKey.jobKey(this.getClass().getName(),"initTaskJobGroup"));
		setTRIGGER_KEY(TriggerKey.triggerKey(this.getClass().getName(),"initTaskJobGroup"));
		setJobDetail(JobBuilder.newJob(ConnectionCheckTask.class).withIdentity(getJobKey()).build());
		setTrigger(TriggerBuilder.newTrigger()
				.withIdentity(getTriggerKey())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds((int) TaskTime.CONNECTION_CHECK_INTERVAL.getTime())//3秒一次检测连接
						.repeatForever())
				.startAt(new Date(System.currentTimeMillis()+ TimeUnit.SECONDS.toMillis(TaskTime.CONNECTION_CHECK_DELAY.getTime())))
				.build());
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		FeifeiPoolDatasource datasource= (FeifeiPoolDatasource) context.getJobDetail().getJobDataMap().get(KeyString.DATASOURCE);
		datasource.checkConnectionValid();
	}
}
