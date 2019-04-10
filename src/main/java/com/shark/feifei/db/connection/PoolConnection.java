package com.shark.feifei.db.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Pool connection
 * @Author: Shark Chili
 * @Date: 2018/9/4 0004
 */
class PoolConnection implements Comparable<PoolConnection>{
	/**data base connection*/
	private Connection connection;
	private long createTime;
	private long updateTime;
	private Status status;

	PoolConnection(Connection connection) {
		this.connection = connection;
		this.createTime=System.currentTimeMillis();
		this.updateTime=System.currentTimeMillis();
		this.status= Status.IDLE;
		try {
			this.connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * to use this connection.
	 * @return this PoolConnection
	 */
	PoolConnection use(){
		this.setStatus(Status.USING);
		this.setUpdateTime(System.currentTimeMillis());
		return this;
	}

	/**
	 * to release this connection after used.(commit)
	 * @return this PoolConnection
	 */
	PoolConnection release(){
		this.setStatus(Status.IDLE);
		this.setUpdateTime(System.currentTimeMillis());
		// commit
		try {
			this.connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}

	Connection getConnection() {
		return connection;
	}

	void setConnection(Connection connection) {
		this.connection = connection;
	}

	long getCreateTime() {
		return createTime;
	}

	void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	long getUpdateTime() {
		return updateTime;
	}

	void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	Status getStatus() {
		return status;
	}

	void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public int compareTo(PoolConnection o) {
		return (int) (this.getUpdateTime()-o.getUpdateTime());
	}

	enum Status{
		/**waiting for use*/
		IDLE,
		/**is using*/
		USING
	}

	@Override
	public String toString() {
		return "PoolConnection{" +
				"connection=" + connection +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", status=" + status +
				'}';
	}
}
