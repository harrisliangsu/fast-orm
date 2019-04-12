package com.shark.feifei.consts;

import com.shark.feifei.db.transaction.TransactionScope;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/1 0001
 */
public class FeifeiConfigDefault {

	// database default attributes
	public static final int
			DB_POOL_CONNECT_TIMEOUT=4,
			DB_POOL_CONNECTION_MAX=20,
			DB_POOL_CONNECTION_INIT=5,
			DB_POOL_CONNECTION_IDLE_TIME=5,
			DB_POOL_CONNECTION_ADD_NUM_ONCE_TIME=10;

	// cluster default attributes
	public static final boolean
			DB_CLUSTER=false,
			DB_CLUSTER_MASTER=false,
			DB_CLUSTER_SLAVE=true,
			DB_CLUSTER_PARTITION_MANAGER=false;

	public static final String DB_ENV="default";

	public static final String
			http_protocol_http="http",
			http_protocol_https="https";

	// transaction
	public static final TransactionScope TRANSACTION_DEFAULT_SCOPE =TransactionScope.QUERY;
}
