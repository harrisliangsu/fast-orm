package com.shark.feifei.query.cache.data;

import com.shark.feifei.query.consts.QueryOptions;
import com.shark.feifei.query.query.EntityQuery;
import com.shark.feifei.query.query.Query;

import java.util.List;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/2
 */
public class QueryCacheData<T> {
	/**sql*/
	private String sql;
	/**sql parameters*/
	private List<Object> parameters;
	/**query options*/
	private List<QueryOptions> options;
	/**query result*/
	private List<T> result;
	/**result type*/
	private Class resultType;

	public QueryCacheData() {
	}

	public QueryCacheData(Query query){
		this.sql=query.queryData().sql();
		this.parameters=query.queryData().getParameters();
		this.result=query.queryData().getResult();
		this.resultType=query.queryData().getResultType();
		this.options=query.queryData().getOptions();
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Object> getParameters() {
		return parameters;
	}

	public void setParameters(List<Object> parameters) {
		this.parameters = parameters;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public Class getResultType() {
		return resultType;
	}

	public void setResultType(Class resultType) {
		this.resultType = resultType;
	}

	public QueryKey key(){
		return new QueryKey(sql);
	}

	/**
	 * Generate a query
	 * @return query
	 */
	public Query generateQuery(){
		Query query= EntityQuery.create(this.getSql());
		query.queryData().setParameters(this.getParameters());
		query.queryData().setResultType(this.getResultType());
		query.addOption(this.options.toArray(new QueryOptions[]{}));
		return query;
	}
}
