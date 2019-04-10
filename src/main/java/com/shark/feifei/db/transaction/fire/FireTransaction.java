package com.shark.feifei.db.transaction.fire;

import com.shark.feifei.db.transaction.context.TransactionContext;

import java.sql.Connection;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/10 0010
 */
public interface FireTransaction {

	/**
	 * Record connection after getting it
	 * @param connection {@link Connection}
	 */
	public void recordCon(Connection connection);

	/**
	 * Whether release connection or not
	 * @param connection {@link Connection}
	 * @return true if release connection,else false
	 */
	public boolean releaseCon(Connection connection);

	/**
	 * Fire sharing connection
	 * @return a {@link Connection}
	 */
	public Connection fireShareCon();
}
