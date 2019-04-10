package com.shark.feifei.db.transaction;

import com.shark.feifei.FeiFeiBootStrap;
import com.shark.feifei.container.FeiFeiContainer;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/13 0013
 */
public class TransactionUtil {

	public static void sessionCommit(){
		FeiFeiBootStrap.get().<FeiFeiContainer>container().queryConfig().getConnectionGet().sessionCommit();
	}

}
