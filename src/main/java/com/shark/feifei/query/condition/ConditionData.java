package com.shark.feifei.query.condition;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author: Shark Chili
 * @Date: 2018/10/25 0025
 */
class ConditionData {
	/**
	 * storage condition sql
	 */
	StringBuilder conditionSql;
	/**
	 * whether one condition is finished,if true then you can add next condition with nextCondition()
	 */
	boolean oneConditionFinished;
	/**
	 * parameters mapped to ?
	 */
	List<Object> parameters;
	/**
	 * case phrase column name
	 */
	String updateColumn;

	ConditionData() {
		conditionSql = new StringBuilder();
		parameters = Lists.newArrayList();
	}

	@Override
	public String toString() {
		return "ConditionData{" +
				"conditionSql=" + conditionSql +
				", oneConditionFinished=" + oneConditionFinished +
				", parameters=" + parameters +
				", updateColumn='" + updateColumn + '\'' +
				'}';
	}
}
