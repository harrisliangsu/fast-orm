package com.shark.feifei.query.config;

import com.google.common.collect.Lists;
import com.shark.feifei.query.cache.config.CacheConfig;
import com.shark.feifei.query.connection.ConnectionGet;
import com.shark.feifei.query.connection.FeifeiConnectionGet;
import com.shark.feifei.query.consts.DataBaseType;
import com.shark.feifei.query.consts.NameStyle;
import com.shark.feifei.query.consts.QueryOptions;

import java.util.List;

/**
 * <p>Sql config,data base type, query options and so on,apply to query global</p>
 *     <pre>
 *     default config:
 *     1. database: mysql
 *     2. ConnectionGet: {@link FeifeiConnectionGet}
 *     3. Ignore: null
 *     4. NameStyle: LOWER_CAMEL_CASE(indicate database table name style)
 *     5. QueryOptions: null
 *     </pre>
 * @Author: Shark Chili
 * @Date: 2018/10/23 0023
 */
public class QueryConfig {
	/**the package name of your {@link com.shark.feifei.query.entity.Entity}*/
	private String entityPackage;
	/**query options*/
	private List<QueryOptions> queryOptions;
	/**database type(mysql,oracle...)*/
	private DataBaseType dataBaseType;
	/**get connection*/
	private ConnectionGet connectionGet;
	/**converts the table name to the entity name, listing the strings to ignore when going to the field name*/
	private Ignore ignore;
	/**convert table name to entity name, list to field name, how to use (underline -> small hump, big hump -> small hump)*/
	private NameStyle nameStyle;
	// 6.cache config
	private CacheConfig cacheConfig;

	public QueryConfig() {
		queryOptions= Lists.newArrayList();
	}

	public List<QueryOptions> getQueryOptions() {
		return queryOptions;
	}

	/**
	 * Add a query option
	 * @param queryOptions {@link QueryOptions}
	 * @return this {@link QueryConfig}
	 */
	public QueryConfig addQueryOptions(QueryOptions...queryOptions) {
		this.queryOptions.addAll(Lists.newArrayList(queryOptions));
		return this;
	}

	public DataBaseType getDataBaseType() {
		return dataBaseType;
	}

	public QueryConfig setDataBaseType(DataBaseType dataBaseType) {
		this.dataBaseType = dataBaseType;
		return this;
	}

	/**
	 * Set default config<p>
	 */
	private void setDefaultConfig(){
		this.dataBaseType=DataBaseType.defaultType();
		this.queryOptions.addAll(QueryOptions.defaultOptions());
		this.connectionGet= new FeifeiConnectionGet();
		this.ignore=Ignore.defaultIgnore();
		this.nameStyle = NameStyle.LOWER_CAMEL_CASE;
		this.cacheConfig=CacheConfig.defaultConfig();
	}

	/**
	 * <p>Get a default sql config</p>
	 *		<pre>
	 *         dataBaseType: mysql
	 *         queryOptions: empty
	 *         connectionGet: {@link FeifeiConnectionGet}
	 *         ignore: empty
	 *         nameStyle: {@link NameStyle}.LOWER_CAMEL_CASE
	 *         cacheConfig: {@link CacheConfig}.defaultConfig()
	 *     </pre>
	 * @return a default {@link QueryConfig}
	 */
	public static QueryConfig deFaultConfig(){
		QueryConfig queryConfig =new QueryConfig();
		queryConfig.setDefaultConfig();
		return queryConfig;
	}

	public ConnectionGet getConnectionGet() {
		return connectionGet;
	}

	public void setConnectionGet(ConnectionGet connectionGet) {
		this.connectionGet = connectionGet;
	}

	public Ignore getIgnore() {
		return ignore;
	}

	public void setIgnore(Ignore ignore) {
		this.ignore = ignore;
	}

	public NameStyle getNameStyle() {
		return nameStyle;
	}

	public void setNameStyle(NameStyle nameStyle) {
		this.nameStyle = nameStyle;
	}

	public CacheConfig getCacheConfig() {
		return cacheConfig;
	}

	public void setCacheConfig(CacheConfig cacheConfig) {
		this.cacheConfig = cacheConfig;
	}

	public String getEntityPackage() {
		return entityPackage;
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	@Override
	public String toString() {
		return "QueryConfig{" +
				"entityPackage='" + entityPackage + '\'' +
				", queryOptions=" + queryOptions +
				", dataBaseType=" + dataBaseType +
				", connectionGet=" + connectionGet +
				", ignore=" + ignore +
				", nameStyle=" + nameStyle +
				", cacheConfig=" + cacheConfig +
				'}';
	}
}
