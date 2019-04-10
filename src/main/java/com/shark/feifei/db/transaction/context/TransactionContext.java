package com.shark.feifei.db.transaction.context;

import com.shark.feifei.db.transaction.TransactionRecorder;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/10 0010
 */
public interface TransactionContext {
	public TransactionRecorder getRecorder();
}
