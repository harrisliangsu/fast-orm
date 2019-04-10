package com.shark.feifei.data;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/11 0011
 */
public class Recorder {
	/**normal data*/
	private MapTable normalData;
	/**thread data*/
	private ThreadLocal<MapTable> threadData;

	/**lock*/
	private Lock lock=new ReentrantLock();

	public MapTable getNormalData() {
		lock.lock();
		if (this.normalData ==null){
			this.normalData =new MapTable();
		}
		lock.unlock();
		return normalData;
	}

	public ThreadLocal<MapTable> getThreadData() {
		lock.lock();
		if (this.threadData==null){
			this.threadData=new ThreadLocal<>();
			this.threadData.set(new MapTable());
		}
		lock.unlock();
		return threadData;
	}
}
