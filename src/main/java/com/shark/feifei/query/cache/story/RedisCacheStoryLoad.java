package com.shark.feifei.query.cache.story;

import com.alibaba.fastjson.JSON;
import com.shark.feifei.FeiFeiBootStrap;
import com.shark.feifei.container.FeiFeiContainer;
import com.shark.feifei.query.cache.consts.CacheConst;
import com.shark.feifei.query.cache.JobUtil;
import com.shark.feifei.query.cache.data.QueryCache;
import com.shark.feifei.query.cache.data.QueryKey;
import com.shark.feifei.query.cache.fire.FireType;
import com.shark.feifei.query.query.Query;
import com.shark.job.job.ScheduleJob;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * A class implement {@link CacheStoryLoad},indicate the cache will be storied in redis
 *
 * @Author: Shark Chili
 * @Date: 2018/11/21 0021
 */
public class RedisCacheStoryLoad extends AbstractCacheStoryLoad {

	@Override
	public void story(QueryCache queryCache) {
		addThread(queryCache.getCacheStatus(),queryCache.getFireType());
		if (load(queryCache.getQueryCacheData().key(), queryCache.getFireType()) == null) {
			generateJob(queryCache);
		}
		set(queryCache,null);
	}

	private void set(QueryCache queryCache,Thread thread) {
		Jedis jedis = FeiFeiBootStrap.get().<FeiFeiContainer>container().cacheContainer().getResource();
		String key = generateKey(queryCache, thread);
		jedis.set(key, JSON.toJSONString(queryCache));
		// record thread
		if (queryCache.getFireType() == FireType.THREAD) {
			// set thread->query key
			jedis.sadd(threadKey(null), key);
		}
		jedis.close();
	}

	@Override
	public QueryCache load(String sql, FireType fireType) {
		return load(new QueryKey(sql), fireType);
	}

	@Override
	public QueryCache load(Query query, FireType fireType) {
		return load(query.key(), fireType);
	}

	@Override
	public QueryCache load(QueryKey key, FireType fireType) {
		Jedis jedis = FeiFeiBootStrap.get().<FeiFeiContainer>container().cacheContainer().getResource();
		String myKey=generateKey(key, fireType);
		QueryCache queryCache=JSON.parseObject(jedis.get(myKey), QueryCache.class);
		// array need to parse solely
		queryCache.getQueryCacheData().setResult(
				JSON.parseArray(queryCache.getQueryCacheData().getResult().toString(),
						queryCache.getQueryCacheData().getResultType()));
		jedis.close();
		return queryCache;
	}

	@Override
	void generateJob(QueryCache queryCache) {
		QueryCache storyCache = new QueryCache(queryCache) {
			@Override
			public void story(QueryCache queryCache,Thread thread) {
				set(queryCache,thread);
			}
		};
		ScheduleJob refresh = JobUtil.generateJobByQueryCache(storyCache);
		// story thread if fire type equal THREAD
		if (queryCache.getFireType()==FireType.THREAD){
			refresh.getJobDetail().getJobDataMap().put(CacheConst.THREAD_KEY, Thread.currentThread());
		}
		FeiFeiBootStrap.get().<FeiFeiContainer>container().taskContainer().schedule(refresh);
	}

	@Override
	void fireRemoveThread(Thread thread) {
		Jedis jedis = FeiFeiBootStrap.get().<FeiFeiContainer>container().cacheContainer().getResource();
		Set<String> keys=jedis.smembers(threadKey(thread));
		keys.forEach(jedis::del);
		jedis.del(threadKey(thread));
		jedis.close();
	}
}
