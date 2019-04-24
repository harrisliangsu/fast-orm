package com.shark.feifei.query.entity;


import com.shark.feifei.query.condition.Condition;
import com.shark.feifei.query.condition.EntityCondition;
import com.shark.feifei.query.consts.QueryOptions;
import com.shark.feifei.query.consts.Sql;
import com.shark.feifei.query.query.EntityQuery;
import com.shark.feifei.query.query.Query;

import java.util.List;

/**
 * The abstract {@link Entity},supply to query with {@link Entity} self
 * @Author: Shark Chili
 * @Date: 2018/10/29 0029
 */
public abstract class AbstractEntity implements Entity{

	/**
	 * Select records from table according to equal condition by this.
	 * @param <T> type of record
	 * @return a list of record
	 */
	public <T extends Entity> List<T> select(){
		Query<T> query= EntityQuery.<T>create()
				.setResultType((Class<T>) this.getClass())
				.addOption(QueryOptions.AUTO_FROM).select(this);
		return query.query();
	}

	/**
	 * Select a record from table according to equal condition by this.
	 * @param <T> type of record
	 * @return a record
	 */
	public <T extends Entity> T selectSingle(){
		Query<T> query= EntityQuery.<T>create()
				.setResultType((Class<T>) this.getClass())
				.addOption(QueryOptions.AUTO_FROM).select(this);
		return query.singleQuery();
	}

	/**
	 * Update this entity.
	 */
	public void update(){
		Query query= EntityQuery.create()
				.addOption(QueryOptions.UPDATE_OLD_RECORD)
				.update(this);
		query.query();
	}

	/**
	 * Insert this object to table.
	 */
	public void insert(){
		Query query= EntityQuery.create()
				.addOption(QueryOptions.UPDATE_OLD_RECORD)
				.insert(this);
		query.query();
	}

	/**
	 * Delete this object from table.
	 */
	public void delete(){
		Query query= EntityQuery.create().delete(this);
		query.query();
	}

	/**
	 * Count by this entity condition.
	 * 	<pre>
	 * 	eg:
	 * 		Student(name="mike",age=13)
	 * 		query: select count(1) from student where name='mike' and age=13.
	 * 	</pre>
	 * @return long value of count records
	 */
	public long count(){
		Condition condition= EntityCondition.create(this);
		Query<Long> query= EntityQuery.<Long>create()
				.count(Sql.DIGIT_1)
				.from(this.getClass())
				.where(condition);
		return query.singleQuery();
	}

	/**
	 * Select records from table according to equal condition by this and return n records.
	 * @param n count of result record.
	 * @param <T> type of record
	 * @return a list of record
	 */
	public <T extends Entity> List<T> top(int n){
		Query<T> query= EntityQuery.<T>create()
				.setResultType((Class<T>) this.getClass())
				.addOption(QueryOptions.AUTO_FROM)
				.select(this)
				.top(n);
		return query.query();
	}

	/**
	 * Select top one record from table according to equal condition by this
	 * @param <T> type of record
	 * @return top one record
	 */
	public <T extends Entity> T firstTop(){
		Query<T> query= EntityQuery.<T>create()
				.setResultType((Class<T>) this.getClass())
				.addOption(QueryOptions.AUTO_FROM)
				.select(this)
				.top(1);
		return query.singleQuery();
	}
}
