package com.shark.feifei.query.connection;

import com.shark.job.job.ScheduleJob;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public abstract class AbstractConnectionGet implements ConnectionGet{
    /**a connection pool*/
    protected DataSource datasource;
    /**init job*/
    protected ScheduleJob initJobs;

    public AbstractConnectionGet() {
        initDataSource();
    }

    public abstract void initDataSource();

    public Connection get() throws SQLException {
        return datasource.getConnection();
    }

    public DataSource getDatasource(){
        return datasource;
    }

    int getOrDefaultIntValue(Properties properties, String key, int defaultValue){
        if (properties.get(key)==null){
            return defaultValue;
        }else {
            return Integer.parseInt(properties.get(key).toString());
        }
    }
}
