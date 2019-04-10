package com.shark.feifei.query.base;


import com.shark.container.Container;
import com.shark.feifei.FeiFeiBootStrap;
import com.shark.feifei.container.DefaultFeiFeiContainer;
import com.shark.feifei.query.config.QueryConfig;
import com.shark.feifei.query.consts.QueryOptions;
import com.shark.feifei.query.query.Query;
import com.shark.feifei.query.query.SelectTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/10/25 0025
 */
public class BaseQueryTest {
	protected static final Logger LOGGER = LoggerFactory.getLogger(SelectTest.class);
	public Query query;

	/**
	 * If not set QueryConfig,will use default QueryConfig.
	 */
	public void setQueryConfig(QueryConfig queryConfig) {
		queryConfig.addQueryOptions(QueryOptions.AUTO_PRINT_RESULT, QueryOptions.AUTO_FROM);
		// set entity path
		queryConfig.setEntityPackage("com.shark.test.feifei.entity");
	}

	private void launch() {
		QueryConfig queryConfig= QueryConfig.deFaultConfig();
		setQueryConfig(queryConfig);
		// set config
		Container container= new DefaultFeiFeiContainer();
		((DefaultFeiFeiContainer) container).setQueryConfig(queryConfig);
		// launch container
		FeiFeiBootStrap.get().container(container).launch();
	}

	@BeforeMethod(enabled = false)
	public void runBefore(){
		launch();
	}
}
