package com.shark.feifei.query.execute;

import com.shark.feifei.Exception.QueryException;
import com.shark.feifei.query.consts.DataBaseType;


/**
 * A factory fro select {@link SqlExecutor} according to database type
 * @Author: Shark Chili
 * @Date: 2018/10/23 0023
 */
public class SqlExecutorFactory {

	/**
	 * Get sql executor.
	 * @param dataBaseType database type
	 * @return {@link SqlExecutor}
	 */
	public static SqlExecutor getSqlExecutor(DataBaseType dataBaseType){
		switch (dataBaseType){
			case MYSQL:
			case ORACLE:
			case SQL_SERVER: {
				return new DefaultExecutor();
			}
		}
		throw new QueryException("data base type is illegal,{}",dataBaseType);
	}

}
