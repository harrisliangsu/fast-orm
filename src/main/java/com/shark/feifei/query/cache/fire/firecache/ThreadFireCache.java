package com.shark.feifei.query.cache.fire.firecache;

import com.shark.feifei.query.cache.config.CacheConfig;
import com.shark.feifei.query.cache.data.RefreshPlan;
import com.shark.feifei.query.cache.fire.FireAttribute;
import com.shark.feifei.query.query.Query;

/**
 * A fire cache that same thread fire cache
 * @Author: Shark Chili
 * @Date: 2018/11/20 0020
 */
public class ThreadFireCache extends AbstractFireCache{
	public static final FireCache THREAD_FIRE_CACHE=new ThreadFireCache();

	@Override
	public RefreshPlan provideRefreshPlan() {
		return RefreshPlan.defaultPlan();
	}

	@Override
	public FireAttribute provideFireAttribute() {
		return FireAttribute.SATISFY;
	}

	@Override
	public boolean tryFireStory(Query query, CacheConfig cacheConfig) {
		return cacheConfig.getCacheStoryLoad().load(query, cacheConfig.getFireType())==null;
	}
}
