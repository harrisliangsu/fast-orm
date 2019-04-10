package com.shark.feifei.query.cache.story;

import com.shark.feifei.query.cache.data.QueryCache;
import com.shark.feifei.query.cache.fire.FireType;
import com.shark.feifei.query.cache.data.QueryKey;
import com.shark.feifei.query.query.Query;

/**
 * How to story query or load query.
 * @Author: Shark Chili
 * @Date: 2018/11/13 0013
 */
public interface CacheStoryLoad {

	/**
	 * Story cache
	 * @param queryCache {@link QueryCache}
	 */
	public void story(QueryCache queryCache);

	/**
	 * Get cache
	 * @param sql query sql
	 * @param fireType {@link FireType}
	 * @return {@link QueryCache} mapped to this sql
	 */
	public QueryCache load(String sql, FireType fireType);

	/**
	 * Get cache
	 * @param query {@link Query}
	 * @param fireType {@link FireType}
	 * @return {@link QueryCache} mapped to this query
	 */
	public QueryCache load(Query query, FireType fireType);

	/**
	 * Get cache
	 * @param key {@link Query}.key()
	 * @param fireType {@link FireType}
	 * @return {@link QueryCache} mapped to this key
	 */
	public QueryCache load(QueryKey key, FireType fireType);
}
