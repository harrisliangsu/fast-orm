package com.shark.feifei.query.cache.fire.firecache;

import com.shark.feifei.query.cache.config.CacheConfig;
import com.shark.feifei.query.cache.data.RefreshPlan;
import com.shark.feifei.query.cache.fire.FireAttribute;
import com.shark.feifei.query.query.Query;

/**
 * Indicate any query will fire cache
 * @Author: Shark Chili
 * @Date: 2018/11/20
 */
public class AnyFireCache extends AbstractFireCache{
	public static final FireCache ANY_FIRE_CACHE=new AnyFireCache();

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
		return true;
	}
}
