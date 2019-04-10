package com.shark.feifei.query.connection;


import com.shark.container.util.ConfigUtil;
import com.shark.feifei.consts.FeifeiConfigConst;
import com.shark.feifei.consts.FeifeiConfigDefault;
import com.shark.feifei.db.FeifeiPoolDatasource;
import com.shark.feifei.db.transaction.TransactionScope;
import com.shark.job.job.ScheduleJob;
import com.shark.util.util.FileUtil;
import com.shark.util.util.StringUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Use connection pool {@link FeifeiPoolDatasource} to get a connection,and read database properties file: /db.properties
 * @Author: Shark Chili
 * @Date: 2018/10/29 0029
 */
public class FeifeiConnectionGet extends AbstractConnectionGet{

	@Override
	public void release(Connection connection) {
		((FeifeiPoolDatasource)this.datasource).releaseConnectionByScope(connection);
	}

	/**
	 * Read properties file and create a datasource
	 */
	public void initDataSource(){
		Properties properties= FileUtil.readProperties("/"+ FeifeiConfigConst.CONFIG_FILE_DEFAULT);
		if (!properties.isEmpty()){
			String url=(String) properties.get(FeifeiConfigConst.DB_URL);
			String driver=(String) properties.get(FeifeiConfigConst.DB_DRIVER);
			String username=(String) properties.get(FeifeiConfigConst.DB_USERNAME);
			String password=(String) properties.get(FeifeiConfigConst.DB_PASSWORD);
			String database=(String) properties.get(FeifeiConfigConst.DB_DATABASE);
			int connectTimeout= getOrDefaultIntValue(properties, FeifeiConfigConst.DB_POOL_CONNECTION_TIMEOUT, FeifeiConfigDefault.DB_POOL_CONNECT_TIMEOUT);
			int connectionMax= getOrDefaultIntValue(properties, FeifeiConfigConst.DB_POOL_CONNECTION_MAX, FeifeiConfigDefault.DB_POOL_CONNECTION_MAX);
			int connectionInit= getOrDefaultIntValue(properties, FeifeiConfigConst.DB_POOL_CONNECTION_INIT, FeifeiConfigDefault.DB_POOL_CONNECTION_INIT);
			int connectionIdleTime= getOrDefaultIntValue(properties, FeifeiConfigConst.DB_POOL_CONNECTION_IDLE_TIME, FeifeiConfigDefault.DB_POOL_CONNECTION_IDLE_TIME);
			int connectionAddNumOnceTime= getOrDefaultIntValue(properties, FeifeiConfigConst.DB_POOL_CONNECTION_ADD_NUM_ONCE_TIME, FeifeiConfigDefault.DB_POOL_CONNECTION_ADD_NUM_ONCE_TIME);
			TransactionScope scope= (TransactionScope) ConfigUtil.getOrDefault(properties, FeifeiConfigConst.DB_TRANSACTION_SCOPE, TransactionScope.QUERY);

			datasource=new FeifeiPoolDatasource(scope,connectionMax,connectionInit,connectionIdleTime,connectionAddNumOnceTime);
			((FeifeiPoolDatasource)datasource).setUrl(url);
			((FeifeiPoolDatasource)datasource).setDriver(driver);
			((FeifeiPoolDatasource)datasource).setUsername(username);
			((FeifeiPoolDatasource)datasource).setPassword(password);
			((FeifeiPoolDatasource)datasource).setConnectionTimeout(connectTimeout);
			if (!StringUtil.isEmpty(database)){
				((FeifeiPoolDatasource)datasource).setDatabase(database);
			}

			((FeifeiPoolDatasource)datasource).init();
		}
	}



	public List<ScheduleJob> initJobs(){return ((FeifeiPoolDatasource)datasource).getInitJobs();}

	public void sessionCommit(){
		((FeifeiPoolDatasource)datasource).sessionCommit();
	}
}
