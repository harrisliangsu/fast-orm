package com.shark.feifei.db.transaction.fire;

import com.shark.feifei.db.transaction.context.FeiFeiTransactionContext;
import com.shark.feifei.db.transaction.context.TransactionContext;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/11 0011
 */
public abstract class AbstractFireTransaction implements FireTransaction{
	/**transaction context*/
	TransactionContext transactionContext;

	public AbstractFireTransaction() {
		this.transactionContext=new FeiFeiTransactionContext();
	}
}
