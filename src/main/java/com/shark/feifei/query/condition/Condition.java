package com.shark.feifei.query.condition;

import java.util.List;

/**
 * A condition represent by sql where xxx,or xxx,and xxx and so on
 * @Author: Shark Chili
 * @Date: 2018/10/11 0011
 */
public interface Condition<T> {

	/**
	 * It`s equivalent to sql: REGEXP
	 * @param reg regular expression
	 * @return this {@link Condition}
	 */
	Condition<T> regular(String reg);

	/**
	 * It`s equivalent to sql: like 'str'.
	 * @param escape a String contain wildcard or not
	 * @return this {@link Condition}
	 */
	Condition<T> like(String escape);

	/**
	 * It`s equivalent to sql: not like 'str'.
	 * @param escape a String contain wildcard or not
	 * @return this {@link Condition}
	 */
	Condition<T> notLike(String escape);

	/**
	 * Origin != other
	 * @param other other value
	 * @return this {@link Condition}
	 */
	Condition<T> unequal(T other);

	/**
	 * Origin greater than other
	 * @param other other value
	 * @return this {@link Condition}
	 */
	Condition<T> greater(T other);

	/**
	 * Origin greater or equal than other
	 * @param other other value
	 * @return this {@link Condition}
	 */
	Condition<T> greaterEqual(T other);

	/**
	 * Origin less than other
	 * @param other other value
	 * @return this {@link Condition}
	 */
	Condition<T> less(T other);

	/**
	 * Origin less or equal than other
	 * @param other other value
	 * @return this {@link Condition}
	 */
	Condition<T> lessEqual(T other);

	/**
	 * Origin ==other
	 * @param other other value
	 * @return this {@link Condition}
	 */
	Condition<T> equal(T other);

	/**
	 * The condition.
	 * @param value object value
	 * @return this {@link Condition}
	 */
	Condition<T> when(Object value);

	/**
	 * The reverse of when condition phrase.
	 * @param expect object value
	 * @return this {@link Condition}
	 */
	Condition<T> ELSE(Object expect);

	/**
	 * The result of when phrase.
	 * @param expect object value
	 * @return this {@link Condition}
	 */
	Condition<T> then(Object expect);

	/**
	 * One case when condition end.
	 * @return this {@link Condition}
	 */
	Condition<T> end();

	/**
	 * One when then phrase in case when
	 * @param when object value
	 * @param then object value
	 * @return this {@link Condition}
	 */
	Condition<T> whenThen(Object when, Object then);

	/**
	 * One when then phrase in case when,and have many when conditions.
	 * @param then object value
	 * @param whens object value
	 * @return this {@link Condition}
	 */
	Condition<T> whenConditionThen(Object then, Condition... whens);

	/**
	 * One when then else phrase in case when
	 * @param when object value
	 * @param then object value
	 * @param elseValue object value
	 * @return this {@link Condition}
	 */
	Condition<T> whenThenElse(Object when, Object then, Object elseValue);

	/**
	 * One when then else phrase in case when,and have many when conditions.
	 * @param then object value
	 * @param elseValue object value
	 * @param whens conditions
	 * @return this {@link Condition}
	 */
	Condition<T> whenConditionThenElse(Object then, Object elseValue, Condition... whens);

	/**
	 * Add origin after finish pre condition.
	 * @param origin column that is compared
	 * @return this {@link Condition}
	 */
	Condition<T> nextCondition(T origin);

	/**
	 * Get condition string.
	 * @return this {@link Condition} sql
	 */
	String sql();

	/**
	 * Get all parameters.
	 * @return objects that replace placeholder
	 */
	List<Object> parameters();

	/**
	 * Get case when column.
	 * @return the column name in case when phrase
	 */
	String updateColumn();

	/**
	 * Indicate update column for case when.
	 * @param origin the column name in case when phrase
	 * @return this {@link Condition}
	 */
	Condition<T> updateColumn(T origin);

	/**
	 * Finish one condition.
	 * @return this {@link Condition}
	 */
	Condition<T> finishOneCondition();
}
