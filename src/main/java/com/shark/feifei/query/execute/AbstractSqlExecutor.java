package com.shark.feifei.query.execute;


import com.shark.feifei.query.query.Query;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Abstract {@link SqlExecutor}
 * @Author: Shark Chili
 * @Date: 2018/10/15 0015
 */
public abstract class AbstractSqlExecutor implements SqlExecutor {

	@Override
	public <T> List<T> query(Query query) throws SQLException {
		return null;
	}

	@Override
	public <T> List<T> update(Query query) throws SQLException {
		return null;
	}

	protected String getTableName(Connection connection) throws SQLException {
		DatabaseMetaData md = connection.getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		while (rs.next()) {
			String tabaleName=rs.getString(3);
			System.out.println(tabaleName);
		}
		return null;
	}
}
