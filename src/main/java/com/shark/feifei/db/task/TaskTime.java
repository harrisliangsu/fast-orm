package com.shark.feifei.db.task;

/**
 * Task time const
 * @Author: Shark Chili
 * @Date: 2018/11/19 0019
 */
public enum TaskTime {
	THREAD_CHECK_INTERVAL(30),
	THREAD_CHECK_DELAY(5),
	CONNECTION_CHECK_INTERVAL(3),
	CONNECTION_CHECK_DELAY(5);

	long time;

	TaskTime(long time) {
		this.time = time;
	}

	public long getTime() {
		return time;
	}
}
