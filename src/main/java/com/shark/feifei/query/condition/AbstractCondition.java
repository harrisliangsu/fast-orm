package com.shark.feifei.query.condition;


import com.shark.feifei.Exception.QueryException;
import com.shark.feifei.query.consts.Sql;
import com.shark.util.util.StringUtil;

import java.util.List;

/**
 * The abstract {@link Condition} class
 * @Author: Shark Chili
 * @Date: 2018/10/12 0012
 */
public abstract class AbstractCondition<T> implements Condition<T> {
	ConditionData conditionData;

	AbstractCondition() {
		this.conditionData=new ConditionData();
	}

	AbstractCondition(String origin) {
		this();
		this.conditionData.conditionSql.append(origin);
	}

	@Override
	public String sql() {
		return this.conditionData.conditionSql.toString();
	}

	@Override
	public List<Object> parameters() {
		return this.conditionData.parameters;
	}

	@Override
	public Condition<T> end() {
		uniqueKeywordCheck(Sql.END);
		elseCheck();
		this.conditionData.conditionSql.append(Sql.END);
		finishOneCondition();
		return this;
	}

	/**
	 * Check whether else is exist or not.
	 */
	private void elseCheck(){
		boolean existElse=this.conditionData.conditionSql.toString().contains(Sql.ELSE);
		if (this.conditionData.updateColumn==null){
			throw new QueryException("update column is`t initialized {}",this.conditionData.conditionSql);
		}
		if (!existElse){
			this.conditionData.conditionSql.append(Sql.ELSE).append(this.conditionData.updateColumn);
		}
	}

	/**
	 * Check whether slq contain unique keyword.
	 * @param keyword string from {@link Sql}
	 */
	void uniqueKeywordCheck(String keyword){
		boolean existKeyword=this.conditionData.conditionSql.toString().contains(keyword);
		if (existKeyword){
			throw new QueryException("sql phrase only can have one {},{}",keyword,this.conditionData.conditionSql.toString());
		}
	}

	@Override
	public Condition<T> finishOneCondition(){
		this.conditionData.oneConditionFinished=true;
		return this;
	}

	Condition<T> openNextCondition(){
		this.conditionData.oneConditionFinished=false;
		return this;
	}

	String and(Condition...conditions){
		StringBuilder builder=new StringBuilder();
		for (Condition condition : conditions) {
			builder.append(Sql.AND);
			builder.append(condition.sql());
			this.conditionData.parameters.addAll(condition.parameters());
		}
		return builder.toString();
	}

	void caseInit(String column){
		this.conditionData.conditionSql.append(Sql.CASE);
		if (column!=null){
			this.conditionData.conditionSql.append(column);
			this.conditionData.updateColumn =column;
		}
	}

	/**
	 * Add parameters mapped to placeholder ?.
	 * @param parameter condition parameter
	 * @return this {@link Condition}
	 */
	public Condition addParameter(Object parameter){
		this.conditionData.parameters.add(parameter);
		return this;
	}

	void setConditionData(ConditionData conditionData){
		this.conditionData=conditionData;
	}

	@Override
	public String toString() {
		return StringUtil.replaceSymbol(this.conditionData.conditionSql.toString(), Sql.QUESTION,
				this.conditionData.parameters.toArray(new Object[]{}));
	}

	@Override
	public String updateColumn() {
		return conditionData.updateColumn;
	}

}
