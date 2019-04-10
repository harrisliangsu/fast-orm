package com.shark.feifei.query.consts;

import com.shark.container.common.ConfigConst;
import com.shark.feifei.Exception.ConfigException;

/**
 * Data base type,eg: mysql,oracle,mongo
 *
 * @Author: Shark Chili
 * @Date: 2018/10/23 0023
 */
public enum DataBaseType implements ConfigConst {
	NULL("null"),
	MYSQL("mysql"),
	ORACLE("oracle"),
	SQL_SERVER("sqlServer");

	String typeName;

	DataBaseType(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * Default data base type.
	 *
	 * @return {@link DataBaseType}.MYSQL
	 */
	public static DataBaseType defaultType() {
		return MYSQL;
	}

	public String getTypeName() {
		return typeName;
	}

	/**
	 * Get a instance of {@link DataBaseType} by type name
	 * @param typeName type name
	 * @return a instance of DataBaseType
	 */
	public DataBaseType getByName(String typeName){
		for (DataBaseType value : DataBaseType.values()) {
			if (value.typeName.equals(typeName)){
				return value;
			}
		}
		throw new ConfigException("database type name %s is error",typeName);
	}


	@Override
	public ConfigConst getDefault() {
		return MYSQL;
	}
}
