package com.shark.feifei.query.consts;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Record database sql
 * @Author: Shark Chili
 * @Date: 2018/10/12 0012
 */
public class Sql {

	/**SQL keyword*/
	public static final Set<String> KEYWORDS;
	/**first keyword in sql*/
	public static final Set<String> STATEMENT;

	public static final String PRIMARY_KEY_ID="id";

	/**symbol*/
	public static final String
			COMMA =",", QUESTION="?", BRACKET_LEFT="(", BRACKET_RIGHT=")", SEMICOLON=";", COLON=":", POINT=".", PERCENT="%", UNDERLINE="_";

	/**query sql*/
	public static final String
			SELECT=" SELECT ", FROM=" FROM ", WHERE=" WHERE ", OR=" OR ", AND=" AND ",
			INNER_JOIN=" INNER JOIN ", LEFT_JOIN=" LEFT JOIN ", RIGHT_JOIN=" RIGHT JOIN ",ON=" ON ",
			CASE=" CASE ",WHEN=" WHEN ", END=" END ", THEN=" THEN ", ELSE=" ELSE ",
			AS=" AS ",
			GROUP_BY=" GROUP BY ",ORDER_BY=" ORDER BY ", DESC=" DESC ", ASC=" ASC ",
			COUNT=" COUNT ",SUM=" SUM ",AVG=" AVG ",MAX=" MAX ",MIN=" MIN ",
			LIKE=" LIKE ",NOT_LIKE=" NOT LIKE ",REGEXP=" REGEXP ",
			LIMIT=" LIMIT ";

	/**update sql*/
	public static final String
			UPDATE=" UPDATE ",
			INSERT =" INSERT INTO ",
			SET=" SET ",
			VALUES =" VALUES ",
			DELETE =" DELETE ";

	public static final String
			ONE_EQUAL_ONE="1=1",
			UPDATE_ID="@update_id";

	/**mysql method*/
	public static final String METHOD_LAST_INSERT_ID="LAST_INSERT_ID()";

	/**compare operator*/
	public static final String
			GREATER=" > ", LESS=" < ", EQUAL=" =  ", UNEQUAL=" <> ", GREATER_EQUAL=" >= ", LESS_EQUAL=" <= ", COLON_EQUAL=" := ";

	/**const value*/
	public static final int DIGIT_0 =0;
	public static final int DIGIT_1 =1;
	public static final String STRING_1 ="1";
	public static final String STAR ="*";

	/**common sql*/
	public static final String
			SELECT_LAST_INSERT_ID =SELECT+METHOD_LAST_INSERT_ID,
			SELECT_UPDATE_ID_EQUAL =SELECT+UPDATE_ID+COLON_EQUAL,
			SELECT_UPDATE_ID =SELECT+UPDATE_ID,
			SET_UPDATE_ID_EQUAL_0=SET+UPDATE_ID+COLON_EQUAL+Sql.QUESTION,
			COUNT_1=COUNT+BRACKET_LEFT+STRING_1+BRACKET_RIGHT;

	static {
		Set<String> keywords=Sets.newHashSet(SELECT,FROM,WHERE,AND,INNER_JOIN,LEFT_JOIN,RIGHT_JOIN,ON,SET);
		// trim()
		KEYWORDS=keywords.stream().map(String::trim).collect(Collectors.toSet());
		STATEMENT =Sets.newHashSet(SELECT.trim(),UPDATE.trim(), INSERT.trim(), DELETE.trim(),SET.trim());
	}

}
