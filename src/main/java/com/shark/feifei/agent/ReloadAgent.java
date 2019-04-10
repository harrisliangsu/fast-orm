package com.shark.feifei.agent;

import java.lang.instrument.Instrumentation;

/**
 * Load class user-defined
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/12/14 0014
 */
public class ReloadAgent {

	public static void premain(String agentArgs, Instrumentation inst){
		GeneralTransformer trans = new GeneralTransformer();
		inst.addTransformer(trans);
	}

}
