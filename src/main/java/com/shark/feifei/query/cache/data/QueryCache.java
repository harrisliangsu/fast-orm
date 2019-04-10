package com.shark.feifei.query.cache.data;

import com.shark.feifei.query.cache.fire.FireType;
import com.shark.feifei.query.query.Query;

/**
 * A cache contain {@link Query} that had execute query()
 * @Author: Shark Chili
 * @Date: 2018/11/13 0013
 */
public class QueryCache<T>{

	private QueryCacheData<T> queryCacheData;
	/**cache version*/
	private CacheVersion version;
	/**cache status*/
	private CacheStatus cacheStatus;
	/**refresh plan*/
	private RefreshPlan refreshPlan;
	/**fire type*/
	private FireType fireType;

	public QueryCache() {
	}

	public QueryCache(QueryCacheData<T> queryCacheData, CacheVersion version, CacheStatus cacheStatus, RefreshPlan refreshPlan, FireType fireType) {
		this.queryCacheData = queryCacheData;
		this.version = version;
		this.cacheStatus = cacheStatus;
		this.refreshPlan = refreshPlan;
		this.fireType = fireType;
	}

	public QueryCache(QueryCache queryCache){
		this.queryCacheData=queryCache.getQueryCacheData();
		this.version=queryCache.getVersion();
		this.cacheStatus=queryCache.getCacheStatus();
		this.refreshPlan=queryCache.getRefreshPlan();
		this.fireType=queryCache.getFireType();
	}

	public CacheVersion getVersion() {
		return version;
	}

	public void setVersion(CacheVersion version) {
		this.version = version;
	}

	public CacheStatus getCacheStatus() {
		return cacheStatus;
	}

	public void setCacheStatus(CacheStatus cacheStatus) {
		this.cacheStatus = cacheStatus;
	}

	public RefreshPlan getRefreshPlan() {
		return refreshPlan;
	}

	public void setRefreshPlan(RefreshPlan refreshPlan) {
		this.refreshPlan = refreshPlan;
	}

	public QueryCacheData<T> getQueryCacheData() {
		return queryCacheData;
	}

	public void setQueryCacheData(QueryCacheData<T> queryCacheData) {
		this.queryCacheData = queryCacheData;
	}

	public FireType getFireType() {
		return fireType;
	}

	public void setFireType(FireType fireType) {
		this.fireType = fireType;
	}

	@Override
	public String toString() {
		return "QueryCache{" +
				"queryCacheData=" + queryCacheData +
				", version=" + version +
				", cacheStatus=" + cacheStatus +
				", refreshPlan=" + refreshPlan +
				", fireType=" + fireType +
				'}';
	}

	/**
	 * Story queryCache again after query again,if you story object in redis,need to override thi method.
	 * @param queryCache {@link QueryCache}
	 * @param thread {@link Thread}
	 */
	public void story(QueryCache queryCache,Thread thread){
		// do something
	}
}
