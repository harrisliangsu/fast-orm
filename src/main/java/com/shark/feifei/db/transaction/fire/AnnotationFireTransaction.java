package com.shark.feifei.db.transaction.fire;

import java.sql.Connection;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/12 0012
 */
public class AnnotationFireTransaction extends AbstractFireTransaction{

	@Override
	public void recordCon(Connection connection) {
		// record
	}

	@Override
	public boolean releaseCon(Connection connection) {
		return false;
	}

	@Override
	public Connection fireShareCon() {
		return null;
	}

}
