package com.shark.feifei.query.execute;


import com.shark.feifei.query.query.Query;

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
}
