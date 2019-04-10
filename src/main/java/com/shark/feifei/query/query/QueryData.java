package com.shark.feifei.query.query;

import com.google.common.collect.Lists;
import com.shark.feifei.FeiFeiBootStrap;
import com.shark.feifei.container.FeiFeiContainer;
import com.shark.feifei.query.condition.Condition;
import com.shark.feifei.query.consts.OperationType;
import com.shark.feifei.query.consts.QueryOptions;
import com.shark.feifei.query.entity.Entity;
import com.shark.feifei.query.execute.SqlExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Shark Chili
 * @Date: 2018/10/25 0025
 */
public class QueryData<T> implements Cloneable{
	/**
	 * storage sql
	 */
	StringBuilder sql;
	/**
	 * multi sql
	 */
	List<StringBuilder> multiSql;
	/**
	 * parameters mapped to ?
	 */
	List<Object> parameters;
	/**
	 * query result
	 */
	List<T> result;
	/**
	 * query option
	 */
	List<QueryOptions> options;
	/**
	 * init condition
	 */
	List<Condition> initCondition;
	/**
	 * all conditions
	 */
	List<Condition> allConditions;
	/**
	 * sql executor
	 */
	SqlExecutor executor;
	/**
	 * result type (necessary to set when columns of select result from multi tables,otherwise the result is a map).
	 */
	Class<T> resultType;
	/**
	 * whether start to execute sql
	 */
	AbstractQuery.SqlStatus sqlStatus;
	/**
	 * indicate the query whether had been storied or not
	 */
	AbstractQuery.QueryStatus queryStatus;
	/**
	 * sql type
	 */
	AbstractQuery.SqlType sqlType;
	/**
	 * sql operation type
	 */
	OperationType operationType;
	/**
	 * sql entity(only apply to single sql type)
	 */
	Entity sqlEntity;
	/**
	 * database connection.
	 */
	private Connection connection;
	/**
	 * is nested query.
	 */
	boolean nestedQuery;
	/**
	 * page data
	 */
	PageData pageData;

	QueryData() {
		this.sql = new StringBuilder();
		this.multiSql = Lists.newArrayList();
		this.parameters = Lists.newArrayList();
		this.result = Lists.newArrayList();
		this.sqlStatus = AbstractQuery.SqlStatus.SQL_WRITING;
		this.queryStatus= AbstractQuery.QueryStatus.NORMAL;
		this.options = Lists.newArrayList();
		this.initCondition = Lists.newArrayList();
	}

	void clear() {
		this.sql = new StringBuilder();

		if (this.multiSql != null) {
			this.multiSql.clear();
		} else {
			this.multiSql = Lists.newArrayList();
		}

		if (this.parameters != null) {
			this.parameters.clear();
		} else {
			this.parameters = Lists.newArrayList();
		}

		if (this.result != null) {
			this.result.clear();
		} else {
			this.result = Lists.newArrayList();
		}

		if (this.options != null) {
			this.options.clear();
		} else {
			this.options = Lists.newArrayList();
		}

		if (this.initCondition != null) {
			this.initCondition.clear();
		} else {
			this.initCondition = Lists.newArrayList();
		}

		this.resultType = null;
		this.sqlStatus = null;
		this.sqlType = null;
		this.operationType = null;
		this.sqlEntity = null;
		this.connection=null;
		this.pageData=null;
	}

	/**
	 * Perform a shallow copy exclude sql
	 * @return a clone of this object
	 * @throws CloneNotSupportedException if clone failed
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		QueryData<T> cloneData= (QueryData<T>) super.clone();
		cloneData.sql=new StringBuilder(sql.toString());
		return cloneData;
	}

	public StringBuilder getSql() {
		return sql;
	}

	public void setSql(StringBuilder sql) {
		this.sql = sql;
	}

	public List<StringBuilder> getMultiSql() {
		return multiSql;
	}

	public void setMultiSql(List<StringBuilder> multiSql) {
		this.multiSql = multiSql;
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

	public List<QueryOptions> getOptions() {
		return options;
	}

	public void setOptions(List<QueryOptions> options) {
		this.options = options;
	}

	public List<Condition> getInitCondition() {
		return initCondition;
	}

	public void setInitCondition(List<Condition> initCondition) {
		this.initCondition = initCondition;
	}

	public SqlExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(SqlExecutor executor) {
		this.executor = executor;
	}

	public Class<T> getResultType() {
		return resultType;
	}

	public void setResultType(Class<T> resultType) {
		this.resultType = resultType;
	}

	public AbstractQuery.SqlStatus getSqlStatus() {
		return sqlStatus;
	}

	public void setSqlStatus(AbstractQuery.SqlStatus sqlStatus) {
		this.sqlStatus = sqlStatus;
	}

	public AbstractQuery.SqlType getSqlType() {
		return sqlType;
	}

	public void setSqlType(AbstractQuery.SqlType sqlType) {
		this.sqlType = sqlType;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public Entity getSqlEntity() {
		return sqlEntity;
	}

	public void setSqlEntity(Entity sqlEntity) {
		this.sqlEntity = sqlEntity;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public boolean isNestedQuery() {
		return nestedQuery;
	}

	public void setNestedQuery(boolean nestedQuery) {
		this.nestedQuery = nestedQuery;
	}

	public PageData getPageData() {
		return pageData;
	}

	public void setPageData(PageData pageData) {
		this.pageData = pageData;
	}

	public List<Condition> getAllConditions() {
		return allConditions;
	}

	public void setAllConditions(List<Condition> allConditions) {
		this.allConditions = allConditions;
	}

	public AbstractQuery.QueryStatus getQueryStatus() {
		return queryStatus;
	}

	public void setQueryStatus(AbstractQuery.QueryStatus queryStatus) {
		this.queryStatus = queryStatus;
	}

	/**
	 * Get a not null connection,if it`s null,get a connection from container
	 * @return {@link Connection}
	 */
	public Connection connection(){
		if (this.connection==null){
			try {
				this.connection= FeiFeiBootStrap.get().<FeiFeiContainer>container().queryConfig().getConnectionGet().get();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return connection;
	}

	/**
	 * Get sql string.
	 * @return a string of sql
	 */
	public String sql(){
		return sql.toString();
	}

	/**
	 * Get multi sql string.
	 * @return a list string of multi sql
	 */
	public List<String> multiSql(){
		return this.multiSql.stream().map(StringBuilder::toString).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return "QueryData{" +
				"sql=" + sql +
				", multiSql=" + multiSql +
				", parameters=" + parameters +
				", result=" + result +
				", options=" + options +
				", initCondition=" + initCondition +
				", allConditions=" + allConditions +
				", executor=" + executor +
				", resultType=" + resultType +
				", sqlStatus=" + sqlStatus +
				", queryStatus=" + queryStatus +
				", sqlType=" + sqlType +
				", operationType=" + operationType +
				", sqlEntity=" + sqlEntity +
				", connection=" + connection +
				", nestedQuery=" + nestedQuery +
				", pageData=" + pageData +
				'}';
	}
}
