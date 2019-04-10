package com.shark.feifei.query.cache;

import com.shark.feifei.Exception.CacheException;
import com.shark.feifei.query.cache.consts.CacheConst;
import com.shark.feifei.query.cache.data.CacheStatus;
import com.shark.feifei.query.cache.data.QueryCache;
import com.shark.feifei.query.cache.data.RefreshPlan;
import com.shark.feifei.query.cache.fire.FireType;
import com.shark.feifei.query.query.AbstractQuery;
import com.shark.feifei.query.query.Query;
import com.shark.job.factory.JobFactory;
import com.shark.job.job.AbstractScheduleJob;
import com.shark.job.job.ScheduleJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

import static com.shark.util.util.LogUtil.LOGGER;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/2
 */
public class JobUtil {

	/**
	 * Generate a job by {@link QueryCache}
	 * @param queryCache {@link QueryCache}
	 * @return a schedule job
	 */
	public static ScheduleJob generateJobByQueryCache(QueryCache queryCache){
		ScheduleJob refresh = new AbstractScheduleJob() {
			@Override
			public void execute(JobExecutionContext context) throws JobExecutionException {
				LOGGER(QueryCache.class).debug("refresh cache start");
				// 若是 fire type为thread,则检测线程是否存在,若不存在,则不再刷新缓存
				QueryCache queryCache = (QueryCache) context.getJobDetail().getJobDataMap().get(CacheConst.QUERY_CACHE_KEY);
				Object threadObj=context.getJobDetail().getJobDataMap().get(CacheConst.THREAD_KEY);
				if (queryCache.getFireType()== FireType.THREAD){
					Thread cacheThread= (Thread) threadObj;
					if (!cacheThread.isAlive()){
						LOGGER(QueryCache.class).debug("thread {} is`t alive,so stop schedule", cacheThread.getId());
						fireStop();
						return;
					}
				}
				// 更新状态
				queryCache.setCacheStatus(CacheStatus.REFRESHING);
				// 重新查询
				Query refreshQuery=queryCache.getQueryCacheData().generateQuery();
				refreshQuery.queryData().setQueryStatus(AbstractQuery.QueryStatus.CACHE);
				List<?> result=refreshQuery.query();
				queryCache.getQueryCacheData().setResult(result);
				// 重新存储一次
				queryCache.story(queryCache, threadObj==null?null: (Thread) threadObj);
				// 更新状态
				queryCache.setCacheStatus(CacheStatus.APPLYING);
				LOGGER(QueryCache.class).debug("refresh cache end");
			}
		};
		ScheduleJob job = JobUtil.generateJobByRefreshPlan(queryCache.getRefreshPlan(),refresh,CacheConst.CACHE_JOB_NAME,queryCache);
		if (job != null) return job;
		throw new CacheException("refresh {} plan does`t be supported temporarily,eg: start,end,period or start,period,executeTime or start,period", queryCache.getRefreshPlan());
	}

	/**
	 * Generate a job according to the refresh plan
	 * @param refreshPlan {@link RefreshPlan}
	 * @param refresh what to do
	 * @param name job name
	 * @param objects will be storied to job
	 * @return a job
	 */
	public static ScheduleJob generateJobByRefreshPlan(RefreshPlan refreshPlan, ScheduleJob refresh, String name,Object...objects) {
		Date start = refreshPlan.getStart();
		Date end = refreshPlan.getEnd();
		Long period = refreshPlan.getPeriod();
		Integer executeTimes = refreshPlan.getExecuteTimes();

		if (start != null && end != null && period != null) {
			ScheduleJob job = JobFactory.startAtDate(start, end, period, refresh, name);
			for (Object object : objects) {
				job.getJobDetail().getJobDataMap().put(CacheConst.QUERY_CACHE_KEY, object);
			}
			return job;
		}

		if (start != null && period != null && executeTimes != null) {
			ScheduleJob job = JobFactory.startAtDate(start, period, executeTimes, refresh, name);
			for (Object object : objects) {
				job.getJobDetail().getJobDataMap().put(CacheConst.QUERY_CACHE_KEY, object);
			}
			return job;
		}

		if (start != null && period != null) {
			ScheduleJob job = JobFactory.startAtDate(start, null, period, refresh, name);
			for (Object object : objects) {
				job.getJobDetail().getJobDataMap().put(CacheConst.QUERY_CACHE_KEY, object);
			}
			return job;
		}
		return null;
	}

}
