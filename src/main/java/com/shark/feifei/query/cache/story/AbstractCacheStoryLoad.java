package com.shark.feifei.query.cache.story;

import com.google.common.collect.Sets;
import com.shark.feifei.FeiFeiBootStrap;
import com.shark.feifei.container.FeiFeiContainer;
import com.shark.feifei.query.cache.JobUtil;
import com.shark.feifei.query.cache.consts.TimeConst;
import com.shark.feifei.query.cache.data.CacheStatus;
import com.shark.feifei.query.cache.data.QueryCache;
import com.shark.feifei.query.cache.data.QueryKey;
import com.shark.feifei.query.cache.data.RefreshPlan;
import com.shark.feifei.query.cache.fire.FireType;
import com.shark.job.job.AbstractScheduleJob;
import com.shark.job.job.ScheduleJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The abstract {@link CacheStoryLoad} class
 * @Author: Shark Chili
 * @Date: 2018/11/20 0020
 */
public abstract class AbstractCacheStoryLoad implements CacheStoryLoad{
	private static final Logger LOGGER= LoggerFactory.getLogger(AbstractCacheStoryLoad.class);

	/**record thread*/
	private Set<Thread> threads;
	/**no fair lock*/
	private Lock lock=new ReentrantLock();

	AbstractCacheStoryLoad() {
		threads= Sets.newHashSet();
	}

	/**
	 * Check not alive thread and remove it.
	 */
	void checkThreadIsAlive(){
		Set<Thread> remove= Sets.newHashSet();
		for (Thread thread : threads) {
			if (!thread.isAlive()){
				remove.add(thread);
			}
		}
		LOGGER.debug("cache remove {} not alive thread",remove.size());
		for (Thread thread : remove) {
			lock.lock();
			threads.remove(thread);
			lock.unlock();
			fireRemoveThread(thread);
		}
	}

	/**
	 * Generate a key
	 * @param queryKey {@link QueryKey}
	 * @param fireType {@link FireType}
	 * @return a string key
	 */
	String generateKey(QueryKey queryKey, FireType fireType){
		if (fireType==FireType.THREAD){
			return threadKey(null)+"&"+queryKey.idetify();
		}else {
			return queryKey.idetify();
		}
	}

	/**
	 * Generate a key string,if cache status is`t refreshing,use current thread
	 * @param queryCache {@link QueryCache}
	 * @param thread {@link Thread}
	 * @return a key string
	 */
	String generateKey(QueryCache queryCache,Thread thread){
		thread=queryCache.getCacheStatus()== CacheStatus.REFRESHING?thread:Thread.currentThread();
		if (queryCache.getFireType()==FireType.THREAD){
			return threadKey(thread)+"&"+queryCache.getQueryCacheData().key().idetify();
		}else {
			return queryCache.getQueryCacheData().key().idetify();
		}
	}

	/**
	 * Get a thread key
	 * @return a string
	 */
	String threadKey(Thread thread){
		if (thread==null) thread=Thread.currentThread();
		return "Thread-"+thread.getId();
	}

	void addThread(CacheStatus status,FireType fireType){
		if (fireType==FireType.THREAD&&status!=CacheStatus.REFRESHING){
			// schedule a job to check thread
			lock.lock();
			if (threads.isEmpty()){
				startCheckThreadTask();
			}
			Thread thread=Thread.currentThread();
			if (!threads.contains(thread)){
				threads.add(thread);
			}
			lock.unlock();
		}
	}

	private void startCheckThreadTask(){
		ScheduleJob job=new AbstractScheduleJob() {
			@Override
			public void execute(JobExecutionContext context) throws JobExecutionException {
				checkThreadIsAlive();
			}
		};
		RefreshPlan refreshPlan=RefreshPlan.defaultPlan();
		refreshPlan.setPeriod(TimeConst.REFRESH_INTERVAL_MINUTES_1);
		ScheduleJob checkJob= JobUtil.generateJobByRefreshPlan(refreshPlan, job, "CheckThreadAlive");
		FeiFeiBootStrap.get().<FeiFeiContainer>container().taskContainer().schedule(checkJob);
	}

	/**
	 * Generate a job for query cache when first cache.
	 * @param queryCache {@link QueryCache}
	 */
	abstract void generateJob(QueryCache queryCache);

	/**
	 * Fire event that remove the thread and remove cache for the thread.
	 * @param thread removed
	 */
	abstract void fireRemoveThread(Thread thread);
}
