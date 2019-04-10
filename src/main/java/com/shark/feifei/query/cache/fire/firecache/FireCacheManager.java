package com.shark.feifei.query.cache.fire.firecache;

import com.shark.feifei.Exception.CacheException;
import com.shark.feifei.query.cache.config.CacheConfig;
import com.shark.feifei.query.cache.data.QueryCache;
import com.shark.feifei.query.query.AbstractQuery;
import com.shark.feifei.query.query.Query;

/**
 * Manage to fire story or load cache
 * @Author: Shark Chili
 * @Date: 2018/11/21 0021
 */
public class FireCacheManager {
	/**
	 * Fire storing cache mapped to the query
	 * @param query {@link Query}
	 * @param cacheConfig {@link CacheConfig}
	 */
	public void fireCacheStory(Query query, CacheConfig cacheConfig){
		// whether query status is cache status
		if (query.queryData().getQueryStatus()== AbstractQuery.QueryStatus.CACHE){
			return;
		}
		if (cacheConfig.getCacheStoryLoad()==null){
			throw new CacheException("cacheStoryLoad had`t set");
		}
		switch (cacheConfig.getFireType()){
			case ANY:{
				AnyFireCache.ANY_FIRE_CACHE.cache(query,cacheConfig);
				break;
			}
			case THREAD:{
				FireCache fireCache=ThreadFireCache.THREAD_FIRE_CACHE;
				if (fireCache.tryFireStory(query,cacheConfig)){
					fireCache.cache(query,cacheConfig);
				}
				break;
			}
			case CONDITION_FIRE:{
				for (FireCache fireCache : cacheConfig.getFireCaches()) {
					if (fireCache.tryFireStory(query,cacheConfig)){
						fireCache.cache(query,cacheConfig);
						break;
					}
				}
				break;
			}
		}
	}

	/**
	 * Fire loading cache mapped to the query
	 * @param query {@link Query}
	 * @param cacheConfig {@link CacheConfig}
	 * @return {@link QueryCache} mapped to this query
	 */
	public QueryCache fireCacheLoad(Query query, CacheConfig cacheConfig){
		if (cacheConfig.getCacheStoryLoad()==null){
			throw new CacheException("cacheStoryLoad had`t set");
		}
		// whether query status is cache status
		if (query.queryData().getQueryStatus()== AbstractQuery.QueryStatus.CACHE){
			return null;
		}
		switch (cacheConfig.getFireType()){
			case ANY:{
				FireCache fireCache=AnyFireCache.ANY_FIRE_CACHE;
				if (fireCache.tryFireLoad(query,cacheConfig)){
					return fireCache.getCache(query,cacheConfig);
				}
				break;
			}
			case THREAD:{
				FireCache fireCache=ThreadFireCache.THREAD_FIRE_CACHE;
				if (fireCache.tryFireLoad(query,cacheConfig)){
					return fireCache.getCache(query,cacheConfig);
				}
				break;
			}
			case CONDITION_FIRE:{
				for (FireCache fireCache : cacheConfig.getFireCaches()) {
					if (fireCache.tryFireLoad(query,cacheConfig)){
						return fireCache.getCache(query,cacheConfig);
					}
				}
				break;
			}
		}
		return null;
	}
}
