package com.shark.feifei.query.cache.fire.firecache;

import com.shark.feifei.query.cache.config.CacheConfig;
import com.shark.feifei.query.cache.data.QueryCache;
import com.shark.feifei.query.cache.data.RefreshPlan;
import com.shark.feifei.query.cache.fire.FireAttribute;
import com.shark.feifei.query.query.Query;

/**
 * Describe who to fire cache,if you custom your FireCache,implement {@link AbstractFireCache}
 * @Author: Shark Chili
 * @Date: 2018/11/19 0019
 */
public interface FireCache {

	/**
	 * The refresh type of cache fired
	 * @return {@link RefreshPlan}
	 */
	RefreshPlan provideRefreshPlan();

	/**
	 * Fire cache attribute
	 * @return {@link FireAttribute}
	 */
	FireAttribute provideFireAttribute();

	/**
	 * Whether the query fire the cache story or not
	 * @param query {@link Query}
	 * @param cacheConfig {@link CacheConfig}
	 * @return whether the query will fire storing cache or not
	 */
	boolean tryFireStory(Query query, CacheConfig cacheConfig);

	/**
	 * Whether the query fire the cache load or not
	 * @param query {@link Query}
	 * @param cacheConfig {@link CacheConfig}
	 * @return whether the query will fire loading(get) cache or not
	 */
	boolean tryFireLoad(Query query, CacheConfig cacheConfig);

	/**
	 * Story query.
	 * @param query {@link Query}
	 * @param cacheConfig {@link CacheConfig}
	 */
	void cache(Query query, CacheConfig cacheConfig);

	/**
	 * Load cache.
	 * @param query {@link Query}
	 * @param cacheConfig {@link CacheConfig}
	 * @return {@link QueryCache} mapped to this query
	 */
	QueryCache getCache(Query query, CacheConfig cacheConfig);
}
