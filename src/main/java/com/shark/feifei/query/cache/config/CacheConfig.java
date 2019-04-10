package com.shark.feifei.query.cache.config;

import com.google.common.collect.Sets;
import com.shark.cache.cache.CacheAppType;
import com.shark.feifei.query.cache.fire.FireType;
import com.shark.feifei.query.cache.fire.firecache.FireCache;
import com.shark.feifei.query.cache.fire.firecache.FireCacheManager;
import com.shark.feifei.query.cache.story.AppCacheStoryLoad;
import com.shark.feifei.query.cache.story.CacheStoryLoad;
import com.shark.feifei.query.cache.story.RedisCacheStoryLoad;

import java.util.Collections;
import java.util.Set;

/**
 * Cache config
 * @Author: Shark Chili
 * @Date: 2018/11/14 0014
 */
public class CacheConfig {
	/**whether to open cache or not*/
	private boolean openCache;
	/**the type of firing cache*/
	private FireType fireType;
	/**customer fire condition*/
	private Set<FireCache> fireCaches;
	/**how to story and load cache,default use app cache*/
	private CacheStoryLoad cacheStoryLoad;
	/**how to story and load cache,default use app cache*/
	private CacheAppType cacheAppType;
	/**fire cache manager*/
	private FireCacheManager fireCacheManager;

	public CacheConfig() {
		fireCacheManager=new FireCacheManager();
		fireCaches=Sets.newHashSet();
	}

	/**
	 * <p>Get a default cache config</p>
	 *     <pre>
	 *         openCache: false
	 *         fireType: FireType.CONDITION_FIRE
	 *         fireCaches: empty
	 *         cacheStoryLoad: {@link AppCacheStoryLoad}
	 *     </pre>
	 * @return {@link CacheConfig}
	 */
	public static CacheConfig defaultConfig(){
		CacheConfig cacheConfig=new CacheConfig();
		cacheConfig.setOpenCache(false);
		cacheConfig.setFireCaches(Sets.newHashSet());
		cacheConfig.setFireType(FireType.CONDITION_FIRE);
		cacheConfig.setCacheAppType(CacheAppType.APP);
		return cacheConfig;
	}

	public CacheStoryLoad getCacheStoryLoad() {
		return cacheStoryLoad;
	}

	private void setCacheStoryLoad(CacheStoryLoad cacheStoryLoad) {
		this.cacheStoryLoad = cacheStoryLoad;
	}

	public boolean isOpenCache() {
		return openCache;
	}

	public void setOpenCache(boolean openCache) {
		this.openCache = openCache;
	}

	public Set<FireCache> getFireCaches() {
		return fireCaches;
	}

	public void setFireCaches(Set<FireCache> fireCaches) {
		this.fireCaches = fireCaches;
	}

	public FireType getFireType() {
		return fireType;
	}

	public void setFireType(FireType fireType) {
		this.fireType = fireType;
	}

	public CacheAppType getCacheAppType() {
		return cacheAppType;
	}

	/**
	 * Set cache application type and set cache story load class.
	 * @param cacheAppType cache application type
	 */
	public void setCacheAppType(CacheAppType cacheAppType) {
		this.cacheAppType = cacheAppType;
		switch (cacheAppType){
			case APP:{
				setCacheStoryLoad(new AppCacheStoryLoad());
				break;
			}
			case REDIS:{
				setCacheStoryLoad(new RedisCacheStoryLoad());
				break;
			}
		}
	}

	/**
	 * Add fire cache
	 * @param fireCaches fireCaches
	 * @return this {@link CacheConfig}
	 */
	public CacheConfig addFireCache(FireCache...fireCaches){
		Collections.addAll(this.fireCaches, fireCaches);
		return this;
	}

	public FireCacheManager getFireCacheManager() {
		return fireCacheManager;
	}

	@Override
	public String toString() {
		return "CacheConfig{" +
				"openCache=" + openCache +
				", fireType=" + fireType +
				", fireCaches=" + fireCaches +
				", cacheAppType=" + cacheAppType +
				", fireCacheManager=" + fireCacheManager +
				'}';
	}
}
