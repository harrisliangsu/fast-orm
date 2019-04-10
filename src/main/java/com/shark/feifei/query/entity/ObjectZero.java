package com.shark.feifei.query.entity;

import com.google.common.collect.Sets;

import java.util.Date;
import java.util.Set;

/**
 * Query will ignore field value and take care field name when field is set const value from {@link ObjectZero}.
 *
 * @Author: Shark Chili
 * @Date: 2018/10/13 0013
 */
public class ObjectZero {
	public static final Byte BYTE =new Byte("0");
	public static final Short SHORT =new Short("0");
	public static final Integer INTEGER =new Integer("0");
	public static final Float FLOAT =new Float("0");
	public static final Double DOUBLE =new Double("0");
	public static final Long LONG =new Long("0");
	public static final Boolean BOOLEAN =new Boolean("null");
	public static final String STRING ="0";
	public static final Date DATE =new Date();

	public static final Set<Object> OBJECT_0 = Sets.newHashSet();

	static {
		OBJECT_0.add(INTEGER);
		OBJECT_0.add(FLOAT);
		OBJECT_0.add(DOUBLE);
		OBJECT_0.add(BYTE);
		OBJECT_0.add(LONG);
		OBJECT_0.add(STRING);
		OBJECT_0.add(DATE);
	}

	public static boolean isObject_0(Object object){
		for (Object o : OBJECT_0) {
			if (o==object) return true;
		}
		return false;
	}
}
