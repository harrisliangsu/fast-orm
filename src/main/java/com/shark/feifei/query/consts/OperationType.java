package com.shark.feifei.query.consts;

/**
 * The type operation of sql
 * @Author: Shark Chili
 * @Date: 2018/10/16
 */
public enum  OperationType {
	/**one sql per query*/
	SINGLE_SQL,
	/**multi sql per query*/
	MULTI_SQL,
	/**batch query*/
	BATCH_SQL;
}
