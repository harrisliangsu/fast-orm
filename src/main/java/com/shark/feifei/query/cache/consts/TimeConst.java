package com.shark.feifei.query.cache.consts;

import java.util.concurrent.TimeUnit;

/**
 * Refresh task time const
 * @Author: Shark Chili
 * @Date: 2018/11/20 0020
 */
public class TimeConst {
	public static final long REFRESH_DELAY= TimeUnit.SECONDS.toMillis(5);
	public static final long REFRESH_INTERVAL_MINUTES_1= TimeUnit.MINUTES.toMillis(1);
	public static final long REFRESH_INTERVAL_HOURS_1= TimeUnit.HOURS.toMillis(1);
	public static final long REFRESH_INTERVAL_DAYS_1= TimeUnit.DAYS.toMillis(1);
}
