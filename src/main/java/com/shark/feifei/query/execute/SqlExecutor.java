package com.shark.feifei.query.execute;

import com.shark.feifei.query.query.Query;

import java.sql.SQLException;
import java.util.List;

/**
 * Executor to execute sql
 * @Author: Shark Chili
 * @Date: 2018/10/15 0015
 */
public interface SqlExecutor {
	/**
	 * Select from db
	 * @param query {@link Query}
	 * @param <T> type record
	 * @return a list of records
	 * @throws SQLException if select failed
	 */
	<T> List<T> query(Query query) throws SQLException;

	/**
	 * Update or delete or insert from db
	 * @param query {@link Query}
	 * @param <T> type of record
	 * @return record updated
	 * @throws SQLException if update failed
	 */
	<T> List<T> update(Query query) throws SQLException;

	/**
	 * Set from db
	 * @param query {@link Query}
	 * @throws SQLException if set failed
	 */
	void set(Query query) throws  SQLException;
}
