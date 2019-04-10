package com.shark.feifei.db.jdbc;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Year;

/**
 * Java type
 * @Author: Shark Chili
 * @Date: 2018/9/30 0030
 */
public class JavaType {
	public static final Class STRING=String.class;
	public static final Class CHAR=char.class;
	public static final Class BIG_DECIMAL = BigDecimal.class;
	public static final Class BOOLEAN= boolean.class;
	public static final Class BYTE= byte.class;
	public static final Class SHORT= short.class;
	public static final Class INT= int.class;
	public static final Class LONG= long.class;
	public static final Class FLOAT= float.class;
	public static final Class DOUBLE= double.class;
	public static final Class BYTE_ARRAY= byte[].class;
	public static final Class DATE= Date.class;
	public static final Class TIME= Time.class;
	public static final Class TIMESTAMP= Timestamp.class;
	public static final Class YEAR= Year.class;
	public static final Class CLOB= Clob.class;
	public static final Class BLOB= Blob.class;
	public static final Class ARRAY= Array.class;
	public static final Class STRUCT= Struct.class;
	public static final Class REF= Ref.class;
	public static final Class URL= java.net.URL.class;
	public static final Class ENUM= Enum.class;
	public static final Class OBJECT= Object.class;
}
