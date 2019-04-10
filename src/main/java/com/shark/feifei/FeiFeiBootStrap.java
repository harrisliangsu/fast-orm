package com.shark.feifei;

import com.shark.container.Container;
import com.shark.container.boot.Boot;
import com.shark.container.boot.Bootstrap;
import com.shark.feifei.container.DefaultFeiFeiContainer;

/**
 * A boot strap of feifei orm
 * @Author: Shark Chili
 * @Date: 2018/11/14
 */
public class FeiFeiBootStrap extends Bootstrap {

	@Override
	public Container provide() {
		return new DefaultFeiFeiContainer();
	}

	/**
	 * Get a singleton FeiFeiBootStrap that status is ZERO
	 * @return a instance of {@link FeiFeiBootStrap}
	 */
	public static FeiFeiBootStrap get() {
		return (FeiFeiBootStrap) InstanceHolder.BOOT;
	}

	/**
	 * Inner class for hold external class instance
	 */
	private static class InstanceHolder {

		private static final Boot BOOT=new FeiFeiBootStrap();

		private void InvocationHandler() {
		}
	}

	/**
	 * The entrance to start
	 * @param args JVM parameters
	 */
	public static void main(String[] args) {
		FeiFeiBootStrap.get().launch();
	}
}
