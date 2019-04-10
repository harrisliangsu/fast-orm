package com.shark.feifei.query.condition;

import com.shark.feifei.Exception.QueryException;
import com.shark.feifei.query.consts.Sql;
import com.shark.util.util.StringUtil;


/**
 * Condition that is satisfy with string phrase,this condition class aim at create sql condition with sql phrase
 * @Author: Shark Chili
 * @Date: 2018/10/12 0012
 */
public class StringCondition<S> extends AbstractCondition<S> {

	StringCondition() {
		super();
	}

	/**
	 * Create a {@link StringCondition}
	 * @param origin the column compared
	 */
	public StringCondition(String origin) {
		super(origin);
	}

	@Override
	public Condition<S> regular(String reg) {
		this.conditionData.conditionSql.append(Sql.REGEXP).append(reg);
		finishOneCondition();
		return null;
	}

	@Override
	public Condition<S> like(String escape) {
		this.conditionData.conditionSql.append(Sql.LIKE).append(escape);
		finishOneCondition();
		return this;
	}

	@Override
	public Condition<S> notLike(String escape) {
		this.conditionData.conditionSql.append(Sql.NOT_LIKE).append(escape);
		finishOneCondition();
		return this;
	}

	@Override
	public Condition<S> unequal(S other) {
		this.conditionData.conditionSql.append(Sql.UNEQUAL).append(other);
		finishOneCondition();
		return this;
	}

	@Override
	public Condition<S> greater(S other) {
		this.conditionData.conditionSql.append(Sql.GREATER).append(other);
		finishOneCondition();
		return this;
	}

	@Override
	public Condition<S> greaterEqual(S other) {
		this.conditionData.conditionSql.append(Sql.GREATER_EQUAL).append(other);
		finishOneCondition();
		return this;
	}

	@Override
	public Condition<S> less(S other) {
		this.conditionData.conditionSql.append(Sql.LESS).append(other);
		finishOneCondition();
		return this;
	}

	@Override
	public Condition<S> lessEqual(S other) {
		this.conditionData.conditionSql.append(Sql.LESS_EQUAL).append(other);
		finishOneCondition();
		return this;
	}

	@Override
	public Condition<S> equal(S other) {
		this.conditionData.conditionSql.append(Sql.EQUAL).append(other);
		finishOneCondition();
		return this;
	}

	@Override
	public Condition<S> nextCondition(S origin) {
		if (this.conditionData.oneConditionFinished){
			this.conditionData.conditionSql.append(Sql.AND).append(origin);
			openNextCondition();
		}else {
			throw new QueryException("pre condition had`t finished",sql());
		}
		return this;
	}

	@Override
	public Condition<S> when(Object value) {
		this.conditionData.conditionSql.append(Sql.WHEN).append(value);
		return this;
	}

	@Override
	public Condition<S> ELSE(Object expect) {
		uniqueKeywordCheck(Sql.ELSE);
		this.conditionData.conditionSql.append(Sql.ELSE).append(expect);
		return this;
	}

	@Override
	public Condition<S> then(Object expect) {
		this.conditionData.conditionSql.append(Sql.THEN).append(expect);
		finishOneCondition();
		return this;
	}

	@Override
	public Condition<S> whenThen(Object when, Object then) {
		StringUtil.append(this.conditionData.conditionSql,Sql.WHEN, (String) when,Sql.THEN, (String) then);
		return this;
	}

	@Override
	public Condition<S> whenConditionThen(Object then, Condition... whens) {
		StringUtil.append(this.conditionData.conditionSql,Sql.WHEN,Sql.ONE_EQUAL_ONE,and(whens),Sql.THEN, (String) then);
		return this;
	}

	@Override
	public Condition<S> whenThenElse(Object when, Object then, Object elseValue) {
		whenThen(when,then);
		this.conditionData.conditionSql.append(Sql.ELSE).append(elseValue);
		return this;
	}

	@Override
	public Condition<S> whenConditionThenElse(Object then, Object elseValue, Condition... whens) {
		whenConditionThen(then,whens);
		this.conditionData.conditionSql.append(Sql.ELSE).append(elseValue);
		return this;
	}

	@Override
	public Condition<S> updateColumn(S origin) {
		this.conditionData.updateColumn = (String) origin;
		return this;
	}

	/**
	 * Normal condition.
	 * @param origin column compared
	 * @return a instance of String Condition
	 */
	public static Condition<String> create(String origin){
		return new StringCondition<>(origin);
	}

	/**
	 * Normal condition and no origin.
	 * @return a instance of String Condition
	 */
	public static Condition<String> create(){
		return new StringCondition<>();
	}

	/**
	 * Select case when.
	 * @param caseColumn the column for case when phrase
	 * @return a instance of String Condition
	 */
	public static Condition<String> caseWhen(String caseColumn){
		StringCondition<String> condition=new StringCondition<>();
		condition.caseInit(caseColumn);
		return condition;
	}

	/**
	 * Update case when
	 * @return a instance of String Condition
	 */
	public static Condition<String> caseWhen(){
		StringCondition<String> condition=new StringCondition<>();
		condition.caseInit(null);
		return condition;
	}
}
