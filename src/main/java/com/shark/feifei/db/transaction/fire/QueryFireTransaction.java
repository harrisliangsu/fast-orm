package com.shark.feifei.db.transaction.fire;

import java.sql.Connection;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/11 0011
 */
public class QueryFireTransaction extends AbstractFireTransaction {

	@Override
	public void recordCon(Connection connection) {
		// get a new connection every query,so don`t record when query
	}

	@Override
	public boolean releaseCon(Connection connection) {
		// release connection every query
		return true;
	}

	@Override
	public Connection fireShareCon() {
		// query don`t share connection
		return null;
	}

	public static FireTransaction create(){
		return new QueryFireTransaction();
	}
}
