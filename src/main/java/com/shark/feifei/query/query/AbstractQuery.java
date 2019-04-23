package com.shark.feifei.query.query;

import com.google.common.collect.Lists;
import com.shark.feifei.Exception.QueryException;
import com.shark.feifei.FeiFeiBootStrap;
import com.shark.feifei.container.FeiFeiContainer;
import com.shark.feifei.query.cache.config.CacheConfig;
import com.shark.feifei.query.cache.data.CacheStatus;
import com.shark.feifei.query.cache.data.QueryCache;
import com.shark.feifei.query.condition.Condition;
import com.shark.feifei.query.config.QueryConfig;
import com.shark.feifei.query.consts.OperationType;
import com.shark.feifei.query.consts.QueryOptions;
import com.shark.feifei.query.consts.Sql;
import com.shark.feifei.query.entity.Entity;
import com.shark.feifei.query.entity.EntityInfo;
import com.shark.feifei.query.entity.EntityUtil;
import com.shark.feifei.query.execute.SqlExecutor;
import com.shark.feifei.query.execute.SqlExecutorFactory;
import com.shark.feifei.query.cache.data.QueryKey;
import com.shark.feifei.query.parse.SqlParse;
import com.shark.util.classes.ClassUtil;
import com.shark.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Abstract {@link Query} class
 * @Author: Shark Chili
 * @Date: 2018/10/12 0012
 */
public abstract class AbstractQuery<T> implements Query<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractQuery.class);

	/**
	 * data
	 */
	QueryData<T> queryData;

	/**
	 * Init result type with generic type,if not set generic type,need to set result type manually,
	 * get {@link SqlExecutor} and get {@link QueryOptions} with {@link QueryConfig}
	 */
	AbstractQuery() {
		queryData = new QueryData<>();
		initResultType();
		// 填充sql config
		parseSqlConfig();
	}

	/**
	 * Create a {@link Query} that {@link OperationType} equal SINGLE_SQL
	 * @param sql init sql phrase
	 */
	AbstractQuery(String sql) {
		this();
		this.queryData.sql.append(sql);
		this.queryData.operationType = OperationType.SINGLE_SQL;
	}

	@Override
	public Query<T> limit(int init, int offset) {
		this.queryData.sql.append(Sql.LIMIT).append(init).append(Sql.COMMA).append(offset);
		return this;
	}

	@Override
	public Query<T> pageSize(int size) {
		if (this.queryData.pageData == null) {
			this.queryData.pageData = new PageData();
			boolean isScroll = this.queryData.options.contains(QueryOptions.QUERY_PAGE_SCROLL);
			boolean isForward = this.queryData.options.contains(QueryOptions.QUERY_PAGE_NO_SCROLL);
			queryData.pageData.isPageScroll = isScroll && !isForward;
		}
		this.queryData.pageData.pageSize = size;
		return this;
	}

	@Override
	public List<T> page(int pageIndex) {
		checkPageDataInit();
		queryData.pageData.page(pageIndex);
		int init = (pageIndex - 1) * this.queryData.pageData.pageSize;
		Query<T> queryPage = null;
		try {
			// shallow copy,and dep copy sql.
			queryPage = (Query<T>) this.clone();
			queryPage.limit(init, this.queryData.pageData.pageSize);
			return queryPage.query();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<T> nextPage() {
		checkPageDataInit();
		int pageIndex = queryData.pageData.nextPageIndex();
		return page(pageIndex);
	}

	@Override
	public List<T> previousPage() {
		checkPageDataInit();
		int pageIndex = queryData.pageData.previousPageIndex();
		return page(pageIndex);
	}

	@Override
	public List<T> firstPage() {
		checkPageDataInit();
		int pageIndex = queryData.pageData.firstPage();
		return page(pageIndex);
	}

	@Override
	public List<T> lastPage() {
		checkPageDataInit();
		int pageIndex = queryData.pageData.lastPage();
		return page(pageIndex);
	}

	/**
	 * Check whether page data had initialized or not
	 */
	private void checkPageDataInit() {
		if (!this.queryData.pageData.initialized) {
			// 查询一次总数
			SqlParse sqlParse = new SqlParse(this.queryData.sql.toString());
			sqlParse.parse();
			String countSql = sqlParse.replaceToNextKeyword(Sql.SELECT, 1, Sql.COUNT_1);
			Query<Long> countQuery = StringQuery.create(countSql);
			// 初始化pageData
			this.queryData.pageData.totalCount = countQuery.singleQuery().intValue();
			try {
				this.queryData.pageData.init();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<T> query() {
		beforeQueryExecute();
		this.queryData.result = execute();
		afterQueryExecute();
		return this.queryData.result;
	}

	private void afterQueryExecute() {
		this.queryData.sqlStatus = SqlStatus.RESULT_RETURN;
		afterQueryOptionDo();
		// 释放连接
		releaseConnection();
		// 缓存
		CacheConfig cacheConfig=FeiFeiBootStrap.get().<FeiFeiContainer>container().queryConfig().getCacheConfig();
		boolean isSingleSql=this.queryData.operationType==OperationType.SINGLE_SQL;
		boolean openCache=cacheConfig.isOpenCache();
		if (isSingleSql&&openCache){
			cacheConfig.getFireCacheManager().fireCacheStory(this,cacheConfig);
		}
	}

	private void beforeQueryExecute() {
		// 判断是否是重复查询
		if (this.queryData.sqlStatus != SqlStatus.RESULT_RETURN) {
			// 添加初始条件
			addInitCondition();
			// 解析sql type
			parseSqlType();
			// 对QueryOption参数处理
			beforeQueryOptionDo();
			this.queryData.sqlStatus = SqlStatus.SQL_FINISH;
		}
	}

	/**
	 * Shallow copy,and dep copy sql.
	 * @return a clone of this object
	 * @throws CloneNotSupportedException if clone failed
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Query<T> query = (Query<T>) super.clone();
		query.setQueryData((QueryData<T>) this.queryData.clone());
		return query;
	}

	@Override
	public QueryData<T> setQueryData(QueryData<T> queryData) {
		return this.queryData = queryData;
	}

	@Override
	public QueryKey key() {
		return new QueryKey(this.queryData.sql());
	}

	@Override
	public Query<T> releaseConnection() {
		// this is`t a nestedQuery
		if (!this.queryData.nestedQuery) {
			FeiFeiBootStrap.get().<FeiFeiContainer>container().queryConfig().getConnectionGet().release(this.queryData.getConnection());
			this.queryData.setConnection(null);
		}
		return this;
	}

	void recordCondition(Condition...conditions){
		if (queryData.allConditions==null){
			queryData.allConditions=Lists.newArrayList();
		}
		queryData.allConditions.addAll(Arrays.asList(conditions));
	}

	/**
	 * Check print sql result and update old entity whether or not
	 */
	private void afterQueryOptionDo() {
		boolean autoPrintResult = this.queryData.options.contains(QueryOptions.AUTO_PRINT_RESULT);
		if (autoPrintResult && !this.queryData.nestedQuery) {
			LOGGER.debug(" query result: {}", this.queryData.result);
		}
		boolean updateOldEntity = this.queryData.options.contains(QueryOptions.UPDATE_OLD_RECORD);
		if (updateOldEntity) {
			EntityUtil.updateEntity(this.queryData.sqlEntity, (Entity) this.queryData.result.get(0));
		}
	}

	/**
	 * Check whether return record id or not {@link OperationType} equal SINGLE_SQL,
	 * if return id that execute sql 'SELECT @update_id:=table.column' in order to get id after execute query
	 */
	private void beforeQueryOptionDo() {
		boolean returnUpdateId = QueryOptions.isReturnId(this.queryData.options);
		if (returnUpdateId && this.queryData.sqlType == SqlType.UPDATE && this.queryData.operationType == OperationType.SINGLE_SQL) {
			Entity entity = this.queryData.sqlEntity;
			EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
			Field field = EntityUtil.getEntityId(entity.getClass());
			if (field == null) {
				field = EntityUtil.getPrimaryKey(entity.getClass());
			}
			if (field == null) {
				throw new QueryException("Entity class {} have no primary key or id", entity.getClass());
			}
			String id = field.getName();
			String selectUpdateId = Sql.SELECT_UPDATE_ID_EQUAL + entityInfo.getTableColumn(id);
			this.queryData.sql.append(Sql.AND).append(Sql.BRACKET_LEFT).append(selectUpdateId).append(Sql.BRACKET_RIGHT);
		}
	}

	/**
	 * Set sql config of query.
	 */
	private void parseSqlConfig() {
		QueryConfig queryConfig = FeiFeiBootStrap.get().<FeiFeiContainer>container().queryConfig();
		this.queryData.executor = SqlExecutorFactory.getSqlExecutor(queryConfig.getDataBaseType());
		this.queryData.options.addAll(queryConfig.getQueryOptions());
	}

	/**
	 * Add init condition(eg: equal condition)
	 */
	private void addInitCondition() {
		if (!this.queryData.initCondition.isEmpty()) {
			boolean existWhere = this.queryData.sql.toString().contains(Sql.WHERE);
			if (existWhere) {
				and(this.queryData.initCondition.toArray(new Condition[]{}));
			} else {
				where(this.queryData.initCondition.toArray(new Condition[]{}));
			}
		}
	}

	@Override
	public T singleQuery() {
		query();
		if (this.queryData.result.isEmpty()) {
			throw new QueryException("no record fro sql: {}", printSql());
		}
		if (this.queryData.result.size() > 1) {
			throw new QueryException("single query result is`t uniqueness {}", this.queryData.result);
		}
		Object singleResult = this.queryData.result.get(0);
		// 无类型结果用map存放,对于单值结果只返回value
		if (Map.class.isAssignableFrom(singleResult.getClass())) {
			Map<String, Object> mapResult = (Map<String, Object>) singleResult;
			if (mapResult.size() == 1) return (T) Lists.newArrayList(mapResult.values()).get(0);
			else return (T) mapResult;
		} else {
			return (T) singleResult;
		}
	}

	@Override
	public Query<T> setExecutor(SqlExecutor executor) {
		this.queryData.executor = executor;
		return this;
	}

	@Override
	public Query<T> caseWhen(Condition... conditions) {
		parseSqlType();
		// use case when condition replace case_column
		for (Condition condition : conditions) {
			String caseColumn = condition.updateColumn();
			boolean existCaseColumn = this.queryData.sql.toString().contains(caseColumn);
			if (!existCaseColumn) {
				throw new QueryException("column {} is`t exist in selectColumns phrase.", caseColumn);
			}
			String columnCaseWhen = null;
			// 判断是select或者update
			if (this.queryData.sqlType == SqlType.SELECT) {
				columnCaseWhen = condition.sql() + Sql.AS + EntityUtil.getFieldName(caseColumn);
			}
			if (this.queryData.sqlType == SqlType.UPDATE) {
				columnCaseWhen = caseColumn + Sql.EQUAL + condition.sql();
			}
			this.queryData.sql = StringUtil.replace(this.queryData.sql, caseColumn, columnCaseWhen);
			this.queryData.parameters.addAll(condition.parameters());
		}
		return this;
	}

	private void parseSqlType() {
		SqlParse sqlParse;
		switch (this.queryData.operationType) {
			case MULTI_SQL: {
				sqlParse = new SqlParse(this.queryData.multiSql.get(0).toString());
				this.queryData.sqlType = SqlType.find(sqlParse.getStatement());
				break;
			}
			case SINGLE_SQL:
			case BATCH_SQL: {
				sqlParse = new SqlParse(this.queryData.sql.toString());
				this.queryData.sqlType = SqlType.find(sqlParse.getStatement());
				break;
			}
			default: {
				throw new QueryException("operation type is`t initialized");
			}
		}
	}

	private void parseOperationType(Serializable... entity) {
		if (entity.length > 1) {
			boolean isBatch = this.queryData.options.contains(QueryOptions.BATCH_OPERATION);
			if (isBatch) {
				this.queryData.operationType = OperationType.BATCH_SQL;
			} else {
				this.queryData.operationType = OperationType.MULTI_SQL;
			}
		} else {
			this.queryData.operationType = OperationType.SINGLE_SQL;
		}
	}

	/**
	 * Add multi targets that is split with COMMA.
	 *
	 * @param targets String objects.
	 */
	void addMultiTarget(String... targets) {
		for (int i = 0; i < targets.length; i++) {
			String column = targets[i];
			this.queryData.sql.append(column);
			if (i != targets.length - 1) {
				this.queryData.sql.append(Sql.COMMA);
			}
		}
	}

	/**
	 * Check whether to execute sql or not
	 */
	void beforeQuery() {
		if (this.queryData.sqlStatus == SqlStatus.SQL_FINISH) {
			throw new QueryException("Query {} had finished,forbid to add query phrases,recommend to invoke query.", this.queryData.getSql());
		}
		if (this.queryData.sqlStatus == SqlStatus.RESULT_RETURN) {
			clear();
		}
	}

	/**
	 * Do something before update,insert,delete.
	 *
	 * @param records records
	 */
	void beforeUpdate(Serializable... records) {
		checkEmptyParameters((Object) records);
		parseOperationType(records);
	}

	/**
	 * Set operation type of selectColumns.
	 */
	void setOperationTypeSelect() {
		this.queryData.operationType = OperationType.SINGLE_SQL;
	}

	/**
	 * Gets the return type through the generic type
	 */
	private void initResultType() {
		if (this.queryData.resultType == null && this.getClass() != StringQuery.class && this.getClass() != EntityQuery.class) {
			this.queryData.resultType = ClassUtil.getSuperGenericClass(this);
		}
	}

	void checkEmptyParameters(Object... parameters) {
		if (parameters == null) {
			throw new QueryException("parameters is null");
		}
		if (parameters.length == 0) {
			throw new QueryException("parameters length is 0");
		}
	}

	void clear() {
		queryData.clear();
	}

	private List<T> execute() {
		if (this.queryData.executor == null) {
			throw new QueryException("sql executor is`t set");
		}
		// print sql
		if (!this.queryData.nestedQuery) {
			LOGGER.debug(printSql());
		}
		// check sql status
		if (!this.queryData.sqlStatus.hadFinishSql()) {
			throw new QueryException("query sql hand`t finished %s,status %s", this.queryData.getSql(), this.queryData.sqlStatus);
		}
		// query cache
		CacheConfig cacheConfig=FeiFeiBootStrap.get().<FeiFeiContainer>container().queryConfig().getCacheConfig();
		boolean openCache=cacheConfig.isOpenCache();
		if (openCache){
			QueryCache queryCache=cacheConfig.getFireCacheManager().fireCacheLoad(this,cacheConfig);
			if (queryCache!=null&&queryCache.getCacheStatus()== CacheStatus.APPLYING){
				LOGGER.debug("use cache query,sql {}",queryData.sql());
				return queryCache.getQueryCacheData().getResult();
			}
		}
		// query database
		try {
			switch (this.queryData.sqlType) {
				case SELECT: {
					return this.queryData.executor.query(this);
				}
				case INSERT:
				case UPDATE:
				case DELETE: {
					return this.queryData.executor.update(this);
				}
				case SET: {
					this.queryData.executor.set(this);
					break;
				}
				default: {
					throw new QueryException("Sql type is`t initial,{}", this.queryData.sqlType);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public QueryData<T> queryData() {
		return this.queryData;
	}

	@Override
	public Query<T> setResultType(Class<T> resultType) {
		this.queryData.resultType = resultType;
		return this;
	}

	@Override
	public Query<T> setConnection(Connection connection) {
		this.queryData.setConnection(connection);
		return this;
	}

	@Override
	public Query<T> setNestedQuery(boolean nestedQuery) {
		this.queryData.nestedQuery = nestedQuery;
		return this;
	}

	@Override
	public Query<T> addOption(QueryOptions... options) {
		this.queryData.options.addAll(Arrays.asList(options));
		return this;
	}

	public QueryData<T> getQueryData() {
		return queryData;
	}

	private String printSql() {
		StringBuilder printSql = new StringBuilder();
		if (this.queryData.operationType == OperationType.SINGLE_SQL) {
			printSql.append(OperationType.SINGLE_SQL).append(Sql.COLON).append(this.queryData.sql.toString());
		} else if (this.queryData.operationType == OperationType.MULTI_SQL) {
			printSql.append(OperationType.MULTI_SQL).append(Sql.COLON).append(this.queryData.multiSql.toString());
		} else if (this.queryData.operationType == OperationType.BATCH_SQL) {
			printSql.append(OperationType.BATCH_SQL).append(Sql.COLON).append(this.queryData.sql);
		}
		return StringUtil.replaceSymbol(printSql.toString(), Sql.QUESTION, this.queryData.parameters.toArray(new Object[]{}));
	}

	void singleSqlNoResultType() {
		setResultType(null);
		this.queryData.operationType = OperationType.SINGLE_SQL;
	}

	/**
	 * Sql status
	 * @Author: Shark Chili
	 * @Date: 2018/10/15 0015
	 */
	public enum SqlStatus {
		/**sql is being wrote*/
		SQL_WRITING,
		/**sql had finished*/
		SQL_FINISH,
		/**result had return*/
		RESULT_RETURN;

		/**
		 * Check whether sql had finished or not
		 * @return if status equal SQL_FINISH or RESULT_RETURN return true,else false
		 */
		public boolean hadFinishSql() {
			return this == SQL_FINISH || this == RESULT_RETURN;
		}
	}

	/**
	 * Sql type
	 * @Author: Shark Chili
	 * @Date: 2018/10/15 0015
	 */
	public enum SqlType {
		INSERT(Sql.INSERT.trim()),
		SELECT(Sql.SELECT.trim()),
		UPDATE(Sql.UPDATE.trim()),
		DELETE(Sql.DELETE.trim()),
		SET(Sql.SET.trim());

		private String sqlName;

		SqlType(String sqlName) {
			this.sqlName = sqlName;
		}

		/**
		 * Find SqlType according to name.
		 *
		 * @param name SqlType name,eg: select or SELECT.
		 * @return SqlType
		 */
		public static SqlType find(String name) {
			for (SqlType value : SqlType.values()) {
				if (name.equals(value.sqlName)) return value;
			}
			for (SqlType value : SqlType.values()) {
				String capital = name.toUpperCase();
				if (capital.equals(value.sqlName)) return value;
			}
			throw new QueryException("no sql type mapped to name: {}", name);
		}
	}

	@Override
	public String toString() {
		return "AbstractQuery{" +
				"queryData=" + queryData +
				'}';
	}

	/**
	 * @Author: Shark Chili
	 * @Email: sharkchili.su@gmail.com
	 * @Date: 2018/12/2
	 */
	public enum  QueryStatus {
		/**normal query that is used by custom*/
		NORMAL,
		/**cache status,query had been storied*/
		CACHE
	}
}
