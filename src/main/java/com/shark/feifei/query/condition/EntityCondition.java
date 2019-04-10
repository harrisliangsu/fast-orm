package com.shark.feifei.query.condition;

import com.shark.feifei.Exception.QueryException;
import com.shark.feifei.FeiFeiBootStrap;
import com.shark.feifei.container.FeiFeiContainer;
import com.shark.feifei.query.consts.Sql;
import com.shark.feifei.query.entity.Entity;
import com.shark.feifei.query.entity.EntityInfo;
import com.shark.feifei.query.entity.EntityUtil;
import com.shark.feifei.query.entity.ObjectZero;
import com.shark.util.util.StringUtil;

import java.util.Map;

/**
 * <p>Condition that is satisfy with comparing column name or column value</p>
 *     <pre>
 *     if Student.create().setAge(ObjectZero.INTEGER): compare column
 *     else compare column value
 *     </pre>
 *     {@link ObjectZero} indicate we take care column name and ignore its value,else column value
 * @Author: Shark Chili
 * @Date: 2018/10/12 0012
 */
public class EntityCondition<T extends Entity, O extends Entity> extends AbstractCondition<O> {

	/**string condition*/
	private StringCondition<String> condition;

	private EntityCondition() {
		super();
		condition = new StringCondition<>();
		copy();
	}

	/**
	 * Origin have only one {@link ObjectZero} field for comparing
	 * @param origin origin
	 */
	private EntityCondition(T origin) {
		this();
		equalCondition(origin);
	}

	private void copy() {
		setConditionData(condition.conditionData);
	}

	/**
	 * If origin have fields that its value is`t {@link ObjectZero},create condition field=value...
	 * @param origin compared object
	 */
	private void equalCondition(Object origin) {
		Entity entity = (Entity) origin;
		EntityInfo entityInfo= FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get((Entity) origin);
		Map<String, Object> field = EntityUtil.getNotNullField(entity);
		// 只允许有一个条件字段
		int object0Count=0;
		String conditionColumn=null;

		String tableName= StringUtil.classLowerHump(origin);
		boolean start = false;
		for (String fieldName : field.keySet()) {
			if (start) this.conditionData.conditionSql.append(Sql.AND);

			Object fieldValue=field.get(fieldName);
			if (ObjectZero.isObject_0(fieldValue)){
				if (object0Count!=0){
					throw new QueryException("Condition only can have one object zero value {}",origin);
				}
				conditionColumn=entityInfo.getTableColumn(fieldName);
				object0Count++;
			}else {
				this.conditionData.conditionSql.append(entityInfo.getTableColumn(fieldName)).append(Sql.EQUAL).append(Sql.QUESTION);
				this.conditionData.parameters.add(fieldValue);
				start = true;
			}
		}
		if (conditionColumn!=null){
			this.conditionData.conditionSql.append(conditionColumn);
		}
	}

	@Override
	public Condition<O> regular(String reg) {
		this.condition.regular(Sql.QUESTION);
		this.conditionData.parameters.add(reg);
		return null;
	}

	@Override
	public Condition<O> like(String escape) {
		this.condition.like(Sql.QUESTION);
		this.conditionData.parameters.add(escape);
		return this;
	}

	@Override
	public Condition<O> notLike(String escape) {
		this.condition.notLike(Sql.QUESTION);
		this.conditionData.parameters.add(escape);
		return this;
	}

	@Override
	public Condition<O> unequal(O other) {
		String firstFieldName = EntityUtil.getFirstTableFieldName(other);
		Object firstFieldValue = EntityUtil.getFirstFieldValue(other);
		if (ObjectZero.isObject_0(firstFieldValue)) {
			condition.unequal(firstFieldName);
		} else {
			// ? 占位
			condition.unequal(Sql.QUESTION);
			condition.conditionData.parameters.add(firstFieldValue);
		}
		return this;
	}

	@Override
	public Condition<O> greater(O other) {
		String firstFieldName = EntityUtil.getFirstTableFieldName(other);
		Object firstFieldValue = EntityUtil.getFirstFieldValue(other);
		if (ObjectZero.isObject_0(firstFieldValue)) {
			condition.greater(firstFieldName);
		} else {
			// ? 占位
			condition.greater(Sql.QUESTION);
			condition.conditionData.parameters.add(firstFieldValue);
		}
		return this;
	}

	@Override
	public Condition<O> greaterEqual(O other) {
		String firstFieldName = EntityUtil.getFirstTableFieldName(other);
		Object firstFieldValue = EntityUtil.getFirstFieldValue(other);
		if (ObjectZero.isObject_0(firstFieldValue)) {
			condition.greaterEqual(firstFieldName);
		} else {
			// ? 占位
			condition.greaterEqual(Sql.QUESTION);
			condition.conditionData.parameters.add(firstFieldValue);
		}
		return this;
	}

	@Override
	public Condition<O> less(O other) {
		String firstFieldName = EntityUtil.getFirstTableFieldName(other);
		Object firstFieldValue = EntityUtil.getFirstFieldValue(other);
		if (ObjectZero.isObject_0(firstFieldValue)) {
			condition.less(firstFieldName);
		} else {
			// ? 占位
			condition.less(Sql.QUESTION);
			condition.conditionData.parameters.add(firstFieldValue);
		}
		return this;
	}

	@Override
	public Condition<O> lessEqual(O other) {
		String firstFieldName = EntityUtil.getFirstTableFieldName(other);
		Object firstFieldValue = EntityUtil.getFirstFieldValue(other);
		if (ObjectZero.isObject_0(firstFieldValue)) {
			condition.lessEqual(firstFieldName);
		} else {
			// ? 占位
			condition.lessEqual(Sql.QUESTION);
			condition.conditionData.parameters.add(firstFieldValue);
		}
		return this;
	}

	@Override
	public Condition<O> equal(O other) {
		String firstFieldName = EntityUtil.getFirstTableFieldName(other);
		Object firstFieldValue = EntityUtil.getFirstFieldValue(other);
		if (ObjectZero.isObject_0(firstFieldValue)) {
			condition.equal(firstFieldName);
		} else {
			// ? 占位
			condition.equal(Sql.QUESTION);
			condition.conditionData.parameters.add(firstFieldValue);
		}
		return this;
	}

	@Override
	public Condition<O> nextCondition(O origin) {
		if (this.conditionData.oneConditionFinished) {
			this.conditionData.conditionSql.append(Sql.AND);
			equalCondition(origin);
			//condition.conditionData.conditionSql.append(Sql.AND).append(EntityUtil.getFirstTableFieldName(origin));
			openNextCondition();
		} else {
			throw new QueryException("pre condition had`t finished", sql());
		}
		return this;
	}

	@Override
	public Condition<O> when(Object column) {
		condition.when(Sql.QUESTION);
		this.conditionData.parameters.add(column);
		return this;
	}

	@Override
	public Condition<O> then(Object expect) {
		condition.then(Sql.QUESTION);
		condition.conditionData.parameters.add(expect);
		return this;
	}

	@Override
	public Condition<O> ELSE(Object expect) {
		uniqueKeywordCheck(Sql.ELSE);
		condition.ELSE(Sql.QUESTION);
		condition.conditionData.parameters.add(expect);
		return this;
	}

	@Override
	public Condition<O> whenThen(Object when, Object then) {
		condition.whenThen(Sql.QUESTION, Sql.QUESTION);
		this.conditionData.parameters.add(when);
		this.conditionData.parameters.add(then);
		return this;
	}

	@Override
	public Condition<O> whenConditionThen(Object then, Condition... whens) {
		condition.whenConditionThen(Sql.QUESTION, whens);
		this.conditionData.parameters.add(then);
		return this;
	}

	@Override
	public Condition<O> whenThenElse(Object when, Object then, Object elseValue) {
		condition.whenThenElse(Sql.QUESTION, Sql.QUESTION, Sql.QUESTION);
		this.conditionData.parameters.add(when);
		this.conditionData.parameters.add(then);
		this.conditionData.parameters.add(elseValue);
		return this;
	}

	@Override
	public Condition<O> whenConditionThenElse(Object then, Object elseValue, Condition... whens) {
		condition.whenConditionThenElse(Sql.QUESTION, Sql.QUESTION, whens);
		this.conditionData.parameters.add(then);
		this.conditionData.parameters.add(elseValue);
		return this;
	}

	@Override
	public Condition<O> updateColumn(O origin) {
		String fieldName=EntityUtil.getFirstTableFieldName(origin);
		condition.updateColumn(fieldName);
		return this;
	}

	/**
	 * Normal condition.<p>
	 * if origin contain field that its value is not object 0, will add equal condition.<p>
	 *     eg: origin= student(name="mike",age=14): name="mike" and age=14.
	 *
	 * @param origin entity contain a field that represented column compared.
	 * @param <O> original type
	 * @return EntityCondition
	 */
	public static <O extends Entity> Condition<O> create(Entity origin) {
		return new EntityCondition<>(origin);
	}

	/**
	 * Create a null condition.
	 * @param <O> original type
	 * @return EntityCondition
	 */
	public static <O extends Entity> Condition<O> create(){
		return new EntityCondition<>();
	}

	/**
	 * Case column when
	 *
	 * @param caseColumn entity contain column that its value is object zero.
	 * @param <O> this original type
	 * @param <T> variable original type
	 * @return EntityCondition
	 */
	public static <T extends Entity,O extends Entity> Condition<O> caseWhen(Entity caseColumn) {
		EntityCondition<T,O> condition = new EntityCondition<>();
		condition.caseInit(EntityUtil.getFirstTableFieldName(caseColumn));
		return condition;
	}

	/**
	 * Case when
	 * @param <T> this original type
	 * @param <O> variable original type
	 * @return EntityCondition.
	 */
	public static <T extends Entity,O extends Entity> Condition<O> caseWhen() {
		EntityCondition<T,O> condition = new EntityCondition<>();
		condition.caseInit(null);
		return condition;
	}
}
