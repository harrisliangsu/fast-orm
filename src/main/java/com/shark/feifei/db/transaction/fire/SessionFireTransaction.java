package com.shark.feifei.db.transaction.fire;

import com.shark.feifei.data.KeyString;

import java.sql.Connection;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/12 0012
 */
public class SessionFireTransaction extends AbstractFireTransaction{

	@Override
	public void recordCon(Connection connection) {
		this.transactionContext.getRecorder().getThreadData().get().putData(KeyString.CONNECTION, connection);
	}

	@Override
	public boolean releaseCon(Connection connection) {
		return false;
	}

	@Override
	public Connection fireShareCon() {
		return this.transactionContext.getRecorder().getThreadData().get().getData(KeyString.CONNECTION);
	}

	public static FireTransaction create(){
		return new SessionFireTransaction();
	}
}
