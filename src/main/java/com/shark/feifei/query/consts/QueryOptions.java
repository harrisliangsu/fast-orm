package com.shark.feifei.query.consts;

import com.google.common.collect.Lists;
import com.shark.feifei.Exception.ConfigException;

import java.util.List;

/**
 * Global query options
 * @Author: Shark Chili
 * @Date: 2018/10/16 0016
 */
public enum  QueryOptions {
	/**return record after querying*/
	RETURN_RECORD("returnRecord"),
	/**return record id after querying*/
	RETURN_ID("returnId"),
	/**indicate batch query*/
	BATCH_OPERATION("batchOperation"),
	/**Auto add from keyword (manual inner join is recommended)*/
	AUTO_FROM("autoFrom"),
	/**Auto print result*/
	AUTO_PRINT_RESULT("autoPrintResult"),
	/**update old entity use record queried*/
	UPDATE_OLD_RECORD("updateOldRecord"),
	/**page query cycle*/
	QUERY_PAGE_SCROLL("queryPageScroll"),
	/**page query only forward or backward*/
	QUERY_PAGE_NO_SCROLL("queryPageNoScroll");

	String optionName;

	QueryOptions(String optionName) {
		this.optionName = optionName;
	}

	/**
	 * <p>Default query options</p>
	 *     <pre>
	 *         AUTO_PRINT_RESULT
	 *         QUERY_PAGE_NO_SCROLL
	 *     </pre>
	 * @return a list of {@link QueryOptions}
	 */
	public static List<QueryOptions> defaultOptions(){
		List<QueryOptions> queryOptions= Lists.newArrayList();
		queryOptions.add(AUTO_PRINT_RESULT);
		queryOptions.add(QUERY_PAGE_NO_SCROLL);
		return queryOptions;
	}

	/**
	 * Whether return id after querying or not
	 * @param options a list of {@link QueryOptions}
	 * @return if options contain RETURN_ID or RETURN_RECORD or UPDATE_OLD_RECORD return true,else false
	 */
	public static boolean isReturnId(List<QueryOptions> options){
		return options.contains(RETURN_ID)||options.contains(RETURN_RECORD)||options.contains(UPDATE_OLD_RECORD);
	}

	/**
	 * Whether return record after querying or not
	 * @param options a list of {@link QueryOptions}
	 * @return if options contain RETURN_RECORD or UPDATE_OLD_RECORD return true,else false
	 */
	public static boolean isReturnRecord(List<QueryOptions> options){
		return options.contains(RETURN_RECORD)||options.contains(UPDATE_OLD_RECORD);
	}

	public String getOptionName() {
		return optionName;
	}

	public static QueryOptions getByName(String optionName){
		for (QueryOptions value : QueryOptions.values()) {
			if (value.getOptionName().equals(optionName)){
				return value;
			}
		}
		throw new ConfigException("query option name %s config error",optionName);
	}
}
