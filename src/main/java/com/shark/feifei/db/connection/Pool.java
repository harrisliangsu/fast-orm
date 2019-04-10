package com.shark.feifei.db.connection;

/**
 * a pool for object(eg: THREAD poo,connection pool)
 * @Author: Shark Chili
 * @Date: 2018/9/4 0004
 */
public interface Pool {
	/**
	 * Get object number that is active.
	 * @return active number
	 */
	public long getActiveNum();

	/**
	 * Get object number that is idle.
	 * @return idle number
	 */
	public long getIdleNum();

	/**
	 * Get object number that is idle or active.
	 * @return total number
	 */
	public long getTotalNum();

	/**
	 * Set object max number
	 * @param maxNum max number
	 */
	public void setObjMaxNum(int maxNum);

	/**
	 * Set object initial created number.
	 * @param initNum init number
	 */
	public void setObjInitNum(int initNum);
}
