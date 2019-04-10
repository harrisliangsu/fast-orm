package com.shark.feifei.db.jdbc;


import com.shark.feifei.Exception.JdbcException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Jdbc type map to java type
 * @Author: Shark Chili
 * @Date: 2018/9/30 0030
 */
public enum  JdbcType {
	BIT(JavaType.BOOLEAN) {
		@Override
		public Object object(String str) {
			return null;
		}
	},
	BOOLEAN(JavaType.BOOLEAN){
		@Override
		public Object object(String str) {
			return Boolean.valueOf(str);
		}
	},
	BOOL(JavaType.BOOLEAN){
		@Override
		public Object object(String str) {
			return Boolean.valueOf(str);
		}
	},
	TINYINT(JavaType.BYTE){
		@Override
		public Object object(String str) {
			return Byte.valueOf(str);
		}
	},
	SMALLINT(JavaType.SHORT){
		@Override
		public Object object(String str) {
			return Short.valueOf(str);
		}
	},
	MEDIUMINT(JavaType.INT){
		@Override
		public Object object(String str) {
			return Integer.valueOf(str);
		}
	},
	INT(JavaType.INT){
		@Override
		public Object object(String str) {
			return Integer.valueOf(str);
		}
	},
	INTEGER(JavaType.INT){
		@Override
		public Object object(String str) {
			return Integer.valueOf(str);
		}
	},
	BIGINT(JavaType.LONG){
		@Override
		public Object object(String str) {
			return Long.valueOf(str);
		}
	},
	DECIMAL(JavaType.BIG_DECIMAL){
		@Override
		public Object object(String str) {
			return new BigDecimal(str);
		}
	},
	NUMERIC(JavaType.BIG_DECIMAL){
		@Override
		public Object object(String str) {
			return new BigDecimal(str);
		}
	},
	DEC(JavaType.BIG_DECIMAL){
		@Override
		public Object object(String str) {
			return new BigDecimal(str);
		}
	},
	REAL(JavaType.FLOAT){
		@Override
		public Object object(String str) {
			return Float.valueOf(str);
		}
	},
	FLOAT(JavaType.FLOAT){
		@Override
		public Object object(String str) {
			return Float.valueOf(str);
		}
	},
	DOUBLE(JavaType.DOUBLE) {
		@Override
		public Object object(String str) {
			return Double.valueOf(str);
		}
	},
	DATE(JavaType.DATE) {
		@Override
		public Object object(String str) {
			return Date.valueOf(str);
		}
	},
	DATETIME(JavaType.DATE) {
		@Override
		public Object object(String str) {
			return Date.valueOf(str);
		}
	},
	TIMESTAMP(JavaType.TIMESTAMP) {
		@Override
		public Object object(String str) {
			return Timestamp.valueOf(str);
		}
	},
	TIME(JavaType.TIME) {
		@Override
		public Object object(String str) {
			return Time.valueOf(str);
		}
	},
	YEAR(JavaType.YEAR) {
		@Override
		public Object object(String str) {
			return null;
		}
	},
	CHAR(JavaType.CHAR) {
		@Override
		public Object object(String str) {
			return str.charAt(0);
		}
	},
	VARCHAR(JavaType.STRING) {
		@Override
		public Object object(String str) {
			return str;
		}
	},
	BINARY(JavaType.BYTE_ARRAY) {
		@Override
		public Object object(String str) {
			return LONGVARBINARY.object(str);
		}
	},
	VARBINARY(JavaType.BYTE_ARRAY) {
		@Override
		public Object object(String str) {
			return LONGVARBINARY.object(str);
		}
	},
	LONGVARBINARY(JavaType.OBJECT){
		@Override
		public Object object(String str) {
			ByteArrayInputStream bytesIn = new ByteArrayInputStream(
					str.getBytes());
			Object object= null;
			try {
				ObjectInputStream objIn = new ObjectInputStream(bytesIn);
				object = objIn.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			return object;
		}
	},
	TINYBLOB(JavaType.BLOB) {
		@Override
		public Object object(String str) {
			// TODO
			return null;
		}
	},
	BLOB(JavaType.BLOB) {
		@Override
		public Object object(String str) {
			return null;
		}
	},
	CLOB(JavaType.CLOB) {
		@Override
		public Object object(String str) {
			return null;
		}
	},
	TEXT(JavaType.STRING) {
		@Override
		public Object object(String str) {
			return str;
		}
	},
	TINYTEXT(JavaType.STRING) {
		@Override
		public Object object(String str) {
			return str;
		}
	},
	MEDIUMBLOB(JavaType.BLOB) {
		@Override
		public Object object(String str) {
			return null;
		}
	},
	MEDIUMTEXT(JavaType.STRING) {
		@Override
		public Object object(String str) {
			return str;
		}
	},
	LONGBLOB(JavaType.BLOB) {
		@Override
		public Object object(String str) {
			return null;
		}
	},
	ENUM(JavaType.STRING) {
		@Override
		public Object object(String str) {
			return str;
		}
	},
	SET(JavaType.ENUM) {
		@Override
		public Object object(String str) {
			return null;
		}
	},
	URL(JavaType.URL){
		@Override
		public Object object(String str) {
			java.net.URL url=null;
			try {
				url=new URL(str);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return url;
		}
	};

	private Class javaType;

	JdbcType(Class javaType) {
		this.javaType = javaType;
	}

	public Class getJavaType() {
		return javaType;
	}

	/**
	 * Get {@link JdbcType} through jdbc type name.
	 * @param type Java type string
	 * @return JdbcType
	 */
	public static JdbcType getJdbcType(String type){
		for (JdbcType value : JdbcType.values()) {
			if (value.name().equals(type)) return value;
		}
		throw new JdbcException("Jdbc type is error: ",type);
	}

	/**
	 * Get {@link JdbcType} through java type.
	 * @param c java type
	 * @return Jdbc type
	 */
	public static JdbcType getJdbcType(Class c){
		for (JdbcType value : JdbcType.values()) {
			if (value.getJavaType()==c) return value;
		}
		throw new JdbcException("Java type is error: ",c);
	}

	/**
	 * Convert string object to object object.
	 * @param str database object string
	 * @return java object
	 */
	public abstract Object object(String str);
}
