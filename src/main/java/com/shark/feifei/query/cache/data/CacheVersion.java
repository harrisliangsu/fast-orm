package com.shark.feifei.query.cache.data;

import java.util.Date;

/**
 * Cache version
 * @Author: Shark Chili
 * @Date: 2018/11/13 0013
 */
public class CacheVersion {
	/**create time*/
	private Date createTime;
	/**update time*/
	private Date updateTime;
	/**when it had ben used last time.*/
	private Date lastUserTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getLastUserTime() {
		return lastUserTime;
	}

	public void setLastUserTime(Date lastUserTime) {
		this.lastUserTime = lastUserTime;
	}

	/**
	 * <p>Create a {@link CacheVersion}</p>
	 *     <pre>
	 *         createTime: now
	 *         updateTIme: now
	 *     </pre>
	 * @return a instance of {@link CacheVersion}
	 */
	public static CacheVersion createNow(){
		CacheVersion version=new CacheVersion();
		version.createTime=new Date(System.currentTimeMillis());
		version.updateTime=new Date(System.currentTimeMillis());
		return version;
	}

	@Override
	public String toString() {
		return "CacheVersion{" +
				"createTime=" + createTime +
				", updateTime=" + updateTime +
				", lastUserTime=" + lastUserTime +
				'}';
	}
}
