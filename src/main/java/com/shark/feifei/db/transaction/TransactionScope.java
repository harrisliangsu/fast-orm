package com.shark.feifei.db.transaction;

import com.shark.container.common.ConfigConst;
import com.shark.feifei.Exception.ConfigException;

/**
 * Connection use scope,default session.
 *
 * @Author: sharkchili
 * @Date: 2018/11/15 0015
 */
public enum TransactionScope implements ConfigConst {
	NULL("null"),
	/**
	 * get a new connection every query
	 */
	QUERY("query"),
	/**
	 * share a same connection in session(or a thread)
	 */
	SESSION("session"),
	/**
	 * user-defined
	 */
	CUSTOMER("customer");

	String scopeName;

	TransactionScope(String scopeName) {
		this.scopeName = scopeName;
	}

	public String getScopeName() {
		return scopeName;
	}

	@Override
	public TransactionScope getByName(String scopeName){
		for (TransactionScope value : TransactionScope.values()) {
			if (scopeName.equals(value.getScopeName())){
				return value;
			}
		}
		throw new ConfigException("transaction scope name %s config error",scopeName);
	}


	@Override
	public ConfigConst getDefault() {
		return QUERY;
	}

}
