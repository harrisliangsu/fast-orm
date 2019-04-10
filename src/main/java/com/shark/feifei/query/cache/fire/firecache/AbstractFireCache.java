package com.shark.feifei.query.cache.fire.firecache;

import com.shark.feifei.query.cache.config.CacheConfig;
import com.shark.feifei.query.cache.data.CacheStatus;
import com.shark.feifei.query.cache.data.CacheVersion;
import com.shark.feifei.query.cache.data.QueryCache;
import com.shark.feifei.query.cache.data.QueryCacheData;
import com.shark.feifei.query.cache.fire.FireAttribute;
import com.shark.feifei.query.condition.Condition;
import com.shark.feifei.query.consts.Sql;
import com.shark.feifei.query.parse.SqlParse;
import com.shark.feifei.query.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * The abstract {@link FireCache} class
 * @Author: Shark Chili
 * @Date: 2018/11/19 0019
 */
public abstract class AbstractFireCache implements FireCache {

	private static final Logger LOGGER= LoggerFactory.getLogger(AbstractFireCache.class);

	@Override
	public boolean tryFireStory(Query query, CacheConfig cacheConfig) {
		boolean tryFire = false;
		// 检测是否已经有该缓存
		if (!checkQueryCacheExist(query,cacheConfig)) return false;
		// 没有该缓存是否满足缓存条件
		FireAttribute fireAttribute = provideFireAttribute();
		if (fireAttribute != null) {

			Set<String> includeTableNames = fireAttribute.getIncludeTableNames();
			if (includeTableNames != null && !includeTableNames.isEmpty()) {
				SqlParse sqlParse = new SqlParse(query.queryData().sql());
				String tableName = sqlParse.findStrAfterKeyword(Sql.FROM, 1);
				tryFire = includeTableNames.contains(tableName.toUpperCase())
						|| includeTableNames.contains(tableName.toLowerCase())
						|| includeTableNames.contains(tableName);
				if (tryFire) {
					LOGGER.debug("includeTableNames {} contain tableName {}",includeTableNames,tableName);
					return tryFire;
				}
			}

			Set<String> excludeTableNames = fireAttribute.getExcludeTableNames();
			if (excludeTableNames != null && !excludeTableNames.isEmpty()) {
				SqlParse sqlParse = new SqlParse(query.queryData().sql());
				String tableName = sqlParse.findStrAfterKeyword(Sql.FROM, 1);
				tryFire = !excludeTableNames.contains(tableName.toUpperCase())
						|| !excludeTableNames.contains(tableName.toLowerCase())
						|| !excludeTableNames.contains(tableName);
				if (tryFire){
					LOGGER.debug("excludeTableNames {} not contain tableName {}",excludeTableNames,tableName);
					return tryFire;
				}
			}

			Set<Condition> containConditions = fireAttribute.getContainConditions();
			if (containConditions != null && !containConditions.isEmpty()) {
				for (Condition condition : containConditions) {
					tryFire = query.queryData().getAllConditions().contains(condition);
					if (tryFire){
						LOGGER.debug("containConditions {} contain condition {}",containConditions,condition);
						return tryFire;
					}
				}
			}

			List<Condition> equalConditions = fireAttribute.getEqualConditions();
			if (equalConditions != null && !equalConditions.isEmpty()) {
				tryFire = equalConditions.equals(query.queryData().getAllConditions());
				if (tryFire){
					LOGGER.debug("equalConditions {} equal all condition {}",equalConditions,query.queryData().getAllConditions());
					return tryFire;
				}
			}

			Set<String> containSqls = fireAttribute.getContainSqls();
			if (containSqls != null && !containSqls.isEmpty()) {
				tryFire = containSqls.contains(query.queryData().sql().toUpperCase())
						|| containSqls.contains(query.queryData().sql().toLowerCase())
						|| containSqls.contains(query.queryData().sql());
				if (tryFire){
					LOGGER.debug("containSqls {} contain sql {}",containSqls,query.queryData().sql());
					return tryFire;
				}
			}
		}
		return tryFire;
	}

	@Override
	public boolean tryFireLoad(Query query, CacheConfig cacheConfig) {
		return cacheConfig.getCacheStoryLoad().load(query, cacheConfig.getFireType())!=null;
	}

	@Override
	public void cache(Query query, CacheConfig cacheConfig) {
		// update query status
		CacheVersion version=CacheVersion.createNow();
		QueryCache cache= new QueryCache(new QueryCacheData(query),version, CacheStatus.APPLYING, provideRefreshPlan(), cacheConfig.getFireType());
		cacheConfig.getCacheStoryLoad().story(cache);
	}

	@Override
	public QueryCache getCache(Query query, CacheConfig cacheConfig) {
		return cacheConfig.getCacheStoryLoad().load(query,cacheConfig.getFireType());
	}

	/**
	 * Whether story cache or not
	 * @param query {@link Query}
	 * @param cacheConfig {@link CacheConfig}
	 * @return if cache is existed mapped to the query return true,otherwise return false
	 */
	private boolean checkQueryCacheExist(Query query, CacheConfig cacheConfig){
		return cacheConfig.getCacheStoryLoad().load(query,cacheConfig.getFireType())==null;
	}
}
