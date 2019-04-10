package com.shark.feifei.db.transaction.context;

import com.shark.feifei.db.transaction.TransactionRecorder;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/11 0011
 */
public abstract class AbstractTransactionContext implements TransactionContext {
	/**record transaction data*/
	private TransactionRecorder recorder;
	/**lock*/
	private Lock lock=new ReentrantLock();

	public TransactionRecorder getRecorder() {
		lock.lock();
		if (recorder==null){
			this.recorder=new TransactionRecorder();
		}
		lock.unlock();
		return recorder;
	}
}
