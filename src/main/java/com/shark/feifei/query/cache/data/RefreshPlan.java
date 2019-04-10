package com.shark.feifei.query.cache.data;

import com.shark.feifei.query.cache.consts.TimeConst;

import java.util.Date;

/**
 * The plan of refresh cache
 * @Author: Shark Chili
 * @Date: 2018/11/14 0014
 */
public class RefreshPlan {
	private Date start;
	private Date end;
	private Long period;
	private Integer executeTimes;

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Long getPeriod() {
		return period;
	}

	public void setPeriod(Long period) {
		this.period = period;
	}

	public Integer getExecuteTimes() {
		return executeTimes;
	}

	public void setExecuteTimes(Integer executeTimes) {
		this.executeTimes = executeTimes;
	}

	/**
	 * <p>Get a default refreshPlan</p>
	 *     <pre>
	 *         start: now + {@link TimeConst}.REFRESH_DELAY
	 *         period: {@link TimeConst}.REFRESH_INTERVAL_HOURS_1
	 *     </pre>
	 * @return {@link RefreshPlan}
	 */
	public static RefreshPlan defaultPlan(){
		RefreshPlan plan=new RefreshPlan();
		Date start=new Date(System.currentTimeMillis()+ TimeConst.REFRESH_DELAY);
		long period= TimeConst.REFRESH_INTERVAL_HOURS_1;
		plan.setStart(start);
		plan.setPeriod(period);
		return plan;
	}

	@Override
	public String toString() {
		return "RefreshPlan{" +
				"start=" + start +
				", end=" + end +
				", period=" + period +
				", executeTimes=" + executeTimes +
				'}';
	}
}
