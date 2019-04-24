package com.shark.feifei.query.query;


import com.shark.feifei.query.condition.Condition;
import com.shark.feifei.query.consts.QueryOptions;
import com.shark.feifei.query.entity.ObjectZero;
import com.shark.feifei.query.execute.SqlExecutor;
import com.shark.feifei.query.cache.data.QueryKey;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

/**
 * Query language that is based sql
 * @Author: Shark Chili
 * @Date: 2018/10/11 0011
 */
public interface Query<T> extends Cloneable{

	/**
	 * Start query and get multi result.
	 * @return a set of records
	 */
	List<T> query();

	/**
	 * Start query and get one result.
	 * @return one record
	 */
	T singleQuery();

	/**
	 * Select all columns.
	 * @param columns selected
	 * @return this {@link Query}
	 */
	Query<T> select(Serializable... columns);

	/**
	 * select record from table <p>
	 *     supported:<p>
	 *
	 *     (1) parameters are columns
	 *         <pre>
	 *             selectColumns(name,age) : selectColumns name,age...
	 *         </pre>
	 *     (2) <p>parameters are entity mapped to table,field mapped to columns that are queried must not to be null,
	 *     		the value is const value from {@link ObjectZero}</p>
	 *		<pre>
	 *     		Student student=new Student();
	 *     		student.setAge(constValue);
	 *		</pre>
	 *     	<p>selectColumns(student) : select age from student...</p>
	 *     (3) <p>parameters are a array that is consist of entity,field mapped to columns that are queried must not to be null,
	 * 	 		the value is const value from {@link ObjectZero}.</p>
	 *		<pre>
	 *     		Student student=new Student();
	 *     		student.setAge(constValue);
	 *     	   	Teacher teacher=new Teacher();
	 *     	   	teacher.setAge(constValue);
	 *		</pre>
	 *     	 <p>selectColumns(student,teacher) : select student.age,teacher.age from student,teacher...</p>
	 *     	(4) <p>parameters are a array tha is consist of entity,field mapped to columns that are queried must not to be null,
	 *     		the value can`t to be const value from {@link ObjectZero}.</p>
	 *     	<pre>
	 *     		Student student=new Student();
	 *     		student.setAge(not_constValue);
	 *     	 </pre>
	 *     	 <p>selectColumns(student) : selectColumns * from student where age=not_constValue...</p>
	 *
	 * @param columns selected
	 * @return this {@link Query}
	 */
	Query<T> selectColumns(Serializable... columns);

	/**
	 * It`s equivalent to sql: from(if you add {@link QueryOptions AUTO_FROM},so need`t to call this method)
	 * @param tables table name
	 * @return this {@link Query}
	 */
	Query<T> from(Class... tables);

	/**
	 * It`s equivalent to sql: where
	 * @param conditions {@link Condition}
	 * @return this {@link Query}
	 */
	Query<T> where(Condition... conditions);

	/**
	 * It`s equivalent to sql: or
	 * @param conditions {@link Condition}
	 * @return this {@link Query}
	 */
	Query<T> or(Condition... conditions);

	/**
	 * It`s equivalent to sql: on
	 * @param conditions {@link Condition}
	 * @return this {@link Query}
	 */
	Query<T> and(Condition... conditions);

	/**
	 * It`s equivalent to sql: on
	 * @param conditions {@link Condition}
	 * @return this {@link Query}
	 */
	Query<T> on(Condition... conditions);

	/**
	 * It`s equivalent to sql: case when
	 * @param conditions {@link Condition}
	 * @return this {@link Query}
	 */
	Query<T> caseWhen(Condition... conditions);

	/**
	 * It`s equivalent to sql: inner join
	 * @param table table name
	 * @return this {@link Query}
	 */
	Query<T> innerJoin(Class table);

	/**
	 * It`s equivalent to sql: left join
	 * @param table table name
	 * @return this {@link Query}
	 */
	Query<T> leftJoin(Class table);

	/**
	 * It`s equivalent to sql: right join
	 * @param table table name
	 * @return this {@link Query}
	 */
	Query<T> rightJoin(Class table);

	/**
	 * Sort by DESC
	 * @param column column name
	 * @return this {@link Query}
	 */
	Query<T> desc(Serializable column);

	/**
	 * Sort by ASC
	 * @param column column name
	 * @return this {@link Query}
	 */
	Query<T> asc(Serializable column);

	/**
	 * It`s equivalent to sql: as
	 * @param column name
	 * @return this {@link Query}
	 */
	Query<T> as(Serializable column);

	/**
	 * Count record.<p>
	 *     eg: column=*,column=1,column=0 (the three phrase is equivalence)
	 *     		column= student(age= const value) (the phrase will ignore null column)
	 * @param column column nmae
	 * @return this {@link Query}
	 */
	Query<T> count(Serializable column);

	/**
	 * Calculate sum column.
	 * @param column column name
	 * @return this {@link Query}
	 */
	Query<T> sum(Serializable column);

	/**
	 * Calculate max column.
	 * @param column column name
	 * @return this {@link Query}
	 */
	Query<T> max(Serializable column);

	/**
	 * Calculate min column.
	 * @param column column name
	 * @return this {@link Query}
	 */
	Query<T> min(Serializable column);

	/**
	 * It`s equivalent to sql: limit
	 * @param init init index,start at 0
	 * @param offset offset
	 * @return this {@link Query}
	 */
	Query<T> limit(int init,int offset);

	/**
	 * Set number of record in one page
	 * @param size page size
	 * @return this {@link Query}
	 */
	Query<T> pageSize(int size);

	/**
	 * Query records in page index.
	 * @param pageIndex page index
	 * @return this {@link Query}
	 */
	List<T> page(int pageIndex);

	/**
	 * Query next page records.
	 * @return this {@link Query}
	 */
	List<T> nextPage();

	/**
	 * Query previous page records.
	 * @return this {@link Query}
	 */
	List<T> previousPage();

	/**
	 * Query first page records.
	 * @return this {@link Query}
	 */
	List<T> firstPage();

	/**
	 * Query last page records.
	 * @return a list of records
	 */
	List<T> lastPage();

	/**
	 * single record insert,multi records insert,batch insert(must add option {@link QueryOptions BATCH_OPERATION})
	 * @param record inserted
	 * @return this {@link Query}
	 */
	Query<T> insert(Serializable... record);

	/**
	 * Update record according to column id.
	 * @param record updated
	 * @return this {@link Query}
	 */
	Query<T> update(Serializable... record);

	/**
	 * Query top n record
	 * @param n count of record
	 * @return this {@link Query}
	 */
	Query<T> top(int n);

	//----------------------------------------------------Query data access------------------------------------------------------------//

	/**
	 * Update record according to column id.
	 * 	if column id is`t exist,according to field from record.
	 * @param record deleted
	 * @return this {@link Query}
	 */
	Query<T> delete(Serializable... record);

	/**
	 * Release the connection.
	 * @return this {@link Query}
	 */
	Query<T> releaseConnection();
	/**
	 * Get a query key,if sql is same,the QueryKey is equal.
	 * @return this {@link Query}
	 */
	QueryKey key();
	/**
	 * Set queryData.
	 * @param queryData {@link QueryData}
	 * @return this {@link Query}
	 */
	QueryData<T> setQueryData(QueryData<T> queryData);
	/**
	 * Query data.
	 * @return this {@link Query}
	 */
	QueryData<T> queryData();
	/**
	 * <p>Set select result type</p>
	 * <h3>It`s necessary to call this method when columns of select result from multi tables,otherwise the result is a map.</h3>
	 * @param resultType the result type of record
	 * @return this {@link Query}
	 */
	Query<T> setResultType(Class<T> resultType);

	/**
	 * Set sql executor.
	 * @param executor {@link SqlExecutor}
	 * @return this {@link Query}
	 */
	Query<T> setExecutor(SqlExecutor executor);

	/**
	 * Add option to query data.
	 * @param options {@link QueryOptions}
	 * @return this {@link Query}
	 */
	Query<T> addOption(QueryOptions... options);

	/**
	 * Set connection.
	 * @param connection {@link Connection}
	 * @return this {@link Query}
	 */
	Query<T> setConnection(Connection connection);

	/**
	 * Set flag whether query is nested or not.
	 * @param nestedQuery true: need to execute other sql if execute this query,else false
	 * @return this {@link Query}
	 */
	Query<T> setNestedQuery(boolean nestedQuery);
}
