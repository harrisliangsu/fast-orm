package com.shark.feifei.query.query;

import com.shark.feifei.query.condition.Condition;
import com.shark.feifei.query.consts.OperationType;
import com.shark.feifei.query.consts.QueryOptions;
import com.shark.feifei.query.consts.Sql;
import com.shark.feifei.query.entity.EntityUtil;
import com.shark.util.util.StringUtil;

import java.io.Serializable;

/**
 * Perform sql query with string
 * @Author: Shark Chili
 * @Date: 2018/10/12 0012
 */
public class StringQuery<T> extends AbstractQuery<T> {

	StringQuery() {
		super();
	}

	StringQuery(String sql) {
		super(sql);
	}

	@Override
	public Query<T> select(Serializable... tables) {
		return selectColumns(tables);
	}

	@Override
	public Query<T> selectColumns(Serializable... tables) {
		beforeQuery();
		setOperationTypeSelect();
		if (this.queryData.sqlType != null) clear();
		this.queryData.sqlType = SqlType.SELECT;

		this.queryData.sql.append(Sql.SELECT);
		addMultiTarget(EntityUtil.convertToString(tables));
		return this;
	}

	@Override
	public Query<T> from(Class... tables) {
		beforeQuery();
		this.queryData.sql.append(Sql.FROM);
		addMultiTarget(EntityUtil.tableNames(tables));
		return this;
	}

	@Override
	public Query<T> where(Condition... conditions) {
		beforeQuery();
		this.queryData.sql.append(Sql.WHERE).append(Sql.ONE_EQUAL_ONE);
		and(conditions);
		return this;
	}

	@Override
	public Query<T> or(Condition... conditions) {
		beforeQuery();
		for (Condition condition : conditions) {
			this.queryData.sql.append(Sql.OR);
			this.queryData.sql.append(condition.sql());
			this.queryData.parameters.addAll(condition.parameters());
		}
		recordCondition(conditions);
		return this;
	}

	@Override
	public Query<T> and(Condition... conditions) {
		beforeQuery();
		for (Condition condition : conditions) {
			this.queryData.sql.append(Sql.AND);
			this.queryData.sql.append(condition.sql());
			this.queryData.parameters.addAll(condition.parameters());
		}
		recordCondition(conditions);
		return this;
	}

	@Override
	public Query<T> innerJoin(Class table) {
		beforeQuery();
		this.queryData.sql.append(Sql.INNER_JOIN).append(StringUtil.classLowerHump(table));
		return this;
	}

	@Override
	public Query<T> leftJoin(Class table) {
		beforeQuery();
		this.queryData.sql.append(Sql.LEFT_JOIN).append(StringUtil.classLowerHump(table));
		return this;
	}

	@Override
	public Query<T> rightJoin(Class table) {
		beforeQuery();
		this.queryData.sql.append(Sql.RIGHT_JOIN).append(StringUtil.classLowerHump(table));
		return this;
	}

	@Override
	public Query<T> on(Condition... conditions) {
		beforeQuery();
		this.queryData.sql.append(Sql.ON).append(Sql.ONE_EQUAL_ONE);
		and(conditions);
		return this;
	}

	@Override
	public Query<T> desc(Serializable column) {
		this.queryData.sql.append(Sql.ORDER_BY).append(column).append(Sql.DESC);
		return null;
	}

	@Override
	public Query<T> asc(Serializable column) {
		this.queryData.sql.append(Sql.ORDER_BY).append(column).append(Sql.ASC);
		return null;
	}

	@Override
	public Query<T> as(Serializable column) {
		this.queryData.sql.append(Sql.AS).append(column);
		return null;
	}

	@Override
	public Query<T> count(Serializable column) {
		StringUtil.append(this.queryData.sql,Sql.SELECT,Sql.COUNT,Sql.BRACKET_LEFT, (String) column,Sql.BRACKET_RIGHT);
		// 清空返回类型
		singleSqlNoResultType();
		return this;
	}

	@Override
	public Query<T> sum(Serializable column) {
		StringUtil.append(this.queryData.sql,Sql.SELECT,Sql.SUM,Sql.BRACKET_LEFT, (String) column,Sql.BRACKET_RIGHT);
		// 清空返回类型
		singleSqlNoResultType();
		return this;
	}

	@Override
	public Query<T> max(Serializable column) {
		StringUtil.append(this.queryData.sql,Sql.SELECT,Sql.MAX,Sql.BRACKET_LEFT, (String) column,Sql.BRACKET_RIGHT);
		// 清空返回类型
		singleSqlNoResultType();
		return this;
	}

	@Override
	public Query<T> min(Serializable column) {
		StringUtil.append(this.queryData.sql,Sql.SELECT,Sql.MIN,Sql.BRACKET_LEFT, (String) column,Sql.BRACKET_RIGHT);
		// 清空返回类型
		singleSqlNoResultType();
		return this;
	}

	@Override
	public Query<T> insert(Serializable... SQLs) {
		updateDo(SQLs);
		return this;
	}

	@Override
	public Query<T> update(Serializable... SQLs) {
		updateDo(SQLs);
		return this;
	}

	@Override
	public Query<T> delete(Serializable... SQLs) {
		updateDo(SQLs);
		return this;
	}

	private void updateDo(Serializable[] SQLs) {
		checkEmptyParameters(SQLs);
		// 是否是批处理,或者单条语句
		if (this.queryData.operationType != OperationType.MULTI_SQL) {
			this.queryData.sql.append(SQLs[0]);
		} else {
			for (Serializable mySQL : SQLs) {
				this.queryData.multiSql.add(new StringBuilder((String) mySQL));
			}
		}
	}

	public static <T> Query<T> create(){
		return new StringQuery<>();
	}

	public static <T> Query<T> create(String sql){
		return new StringQuery<>(sql);
	}

	public static <T> Query<T> createAutoFrom(){
		return StringQuery.<T>create().addOption(QueryOptions.AUTO_FROM);
	}
}
