package com.shark.feifei.query.connection;

import com.shark.job.job.ScheduleJob;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * How to get a database connection.
 * @Author: Shark Chili
 * @Date: 2018/10/29 0029
 */
public interface ConnectionGet {
	/**
	 * Get a connection
	 * @return {@link Connection}
	 * @throws SQLException if get connection failed throw SQLException
	 */
	Connection get() throws SQLException;

	/**
	 * Release a connection
	 * @param connection {@link Connection}
	 */
	void release(Connection connection);

	/**
	 * Get init jobs for datasource
	 * @return a list ScheduleJob
	 */
	public List<ScheduleJob> initJobs();

	/**
	 * Commit the connection belong to a session
	 */
	public void sessionCommit();
}
