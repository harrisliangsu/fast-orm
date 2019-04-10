package com.shark.feifei.db.transaction;

import com.google.common.collect.Lists;
import com.shark.feifei.db.transaction.fire.FireTransaction;
import com.shark.feifei.db.transaction.fire.QueryFireTransaction;
import com.shark.feifei.db.transaction.fire.SessionFireTransaction;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/12 0012
 */
public class TransactionManager {
	/**transaction scope*/
	private TransactionScope scope;
	/**fire transaction instance*/
	private LinkedList<FireTransaction> fireTransactions;

	private TransactionManager() {
		this.fireTransactions = Lists.newLinkedList();
	}

	public TransactionManager(TransactionScope scope) {
		this();
		this.scope=scope;
		init();
	}

	private void init() {
		// add FireTransaction
		switch (this.scope) {
			case QUERY: {
				offerFireTransaction(QueryFireTransaction.create());
				break;
			}
			case SESSION: {
				offerFireTransaction(SessionFireTransaction.create());
			}
		}
	}

	void offerFireTransaction(FireTransaction fireTransaction) {
		this.fireTransactions.offer(fireTransaction);
	}

	void pollFireTransaction(){
		this.fireTransactions.pollLast();
	}

	public void recordCon(Connection connection) {
		this.fireTransactions.getLast().recordCon(connection);
	}

	public boolean releaseCon(Connection connection) {
		// depending on the last FireTransaction
		return this.fireTransactions.getLast().releaseCon(connection);
	}

	public Connection fireShareCon() {
		return this.fireTransactions.getLast().fireShareCon();
	}

	public FireTransaction sessionFireTransaction(){
		for (FireTransaction fireTransaction : this.fireTransactions) {
			if (fireTransaction instanceof SessionFireTransaction){
				return fireTransaction;
			}
		}
		return null;
	}
}
