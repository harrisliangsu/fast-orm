package com.shark.feifei.query.cache.story;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.shark.feifei.FeiFeiBootStrap;
import com.shark.feifei.container.FeiFeiContainer;
import com.shark.feifei.query.cache.JobUtil;
import com.shark.feifei.query.cache.consts.CacheConst;
import com.shark.feifei.query.cache.data.QueryCache;
import com.shark.feifei.query.cache.data.QueryKey;
import com.shark.feifei.query.cache.fire.FireType;
import com.shark.feifei.query.query.Query;
import com.shark.job.job.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A class extends AbstractCacheStoryLoad,indicate the cache will be storied in program
 * @Author: Shark Chili
 * @Date: 2018/11/13 0013
 */
public class AppCacheStoryLoad extends AbstractCacheStoryLoad{
	private static final Logger LOGGER= LoggerFactory.getLogger(AppCacheStoryLoad.class);
	/**no fair lock*/
	private Lock lock=new ReentrantLock();


	/**{@link QueryCache} map,apply to {@link FireType}.ANY*/
	private Map<String,QueryCache> queryCacheMap;
	/**the job that to refresh cache*/
	private Map<String,ScheduleJob> jobMap;

	public AppCacheStoryLoad() {
		queryCacheMap=Maps.newHashMap();
		jobMap=Maps.newHashMap();
	}

	@Override
	public void story(QueryCache queryCache) {
		addThread(queryCache.getCacheStatus(),queryCache.getFireType());
		String key=generateKey(queryCache.getQueryCacheData().key(), queryCache.getFireType());
		lock.lock();
		if (jobMap.get(key)==null){
			queryCacheMap= Maps.newHashMap();
			// 第一次时新建一个任务
			generateJob(queryCache);
		}

		queryCacheMap.put(key,queryCache);
		lock.unlock();
		LOGGER.debug("story cache: {}",queryCache);
	}

	@Override
	public QueryCache load(String sql, FireType fireType) {
		QueryKey key=new QueryKey(sql);
		return load(key,fireType);
	}

	@Override
	public QueryCache load(Query query, FireType fireType){
		QueryKey key=new QueryKey(query.queryData().sql());
		return load(key,fireType);
	}

	@Override
	public QueryCache load(QueryKey key, FireType fireType){
		String myKey=generateKey(key, fireType);
		lock.lock();
		QueryCache queryCache = queryCacheMap.get(myKey);
		lock.unlock();
		return queryCache;
	}

	@Override
	void generateJob(QueryCache queryCache) {
		ScheduleJob refresh= JobUtil.generateJobByQueryCache(queryCache);
		// story thread if fire type equal THREAD
		if (queryCache.getFireType()==FireType.THREAD){
			refresh.getJobDetail().getJobDataMap().put(CacheConst.THREAD_KEY, Thread.currentThread());
		}
		FeiFeiContainer container=FeiFeiBootStrap.get().container();
		container.taskContainer().schedule(refresh);
		String key=generateKey(queryCache.getQueryCacheData().key(), container.queryConfig().getCacheConfig().getFireType());
		jobMap.put(key,refresh);
	}

	@Override
	void fireRemoveThread(Thread thread) {
		Set<String> removeCache= Sets.newHashSet();
		for (String key : queryCacheMap.keySet()) {
			if (key.startsWith("Thread-"+thread.getId())){
				removeCache.add(key);
			}
		}
		// remove
		for (String key : removeCache) {
			lock.lock();
			queryCacheMap.remove(key);
			// stop job
			jobMap.get(key).fireStop();
			jobMap.remove(key);
			lock.unlock();
		}
	}
}
