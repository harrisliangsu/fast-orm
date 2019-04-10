package com.shark.feifei.query.cache;


import com.shark.cache.cache.CacheAppType;
import com.shark.feifei.query.base.BaseQueryTest;
import com.shark.feifei.query.cache.config.CacheConfig;
import com.shark.feifei.query.cache.fire.FireType;
import com.shark.feifei.query.config.QueryConfig;
import com.shark.feifei.query.consts.QueryOptions;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/11/20 0020
 */
public class BaseCacheQueryTest extends BaseQueryTest {

	@Override
	public void setQueryConfig(QueryConfig queryConfig) {
		queryConfig.addQueryOptions(QueryOptions.AUTO_FROM);
		queryConfig.setEntityPackage("com.shark.test.feifei.entity");

		CacheConfig cacheConfig= CacheConfig.defaultConfig();
		cacheConfig.setFireType(FireType.THREAD);
		cacheConfig.setOpenCache(true);
		cacheConfig.setCacheAppType(CacheAppType.REDIS);

		queryConfig.setCacheConfig(cacheConfig);
	}

}
