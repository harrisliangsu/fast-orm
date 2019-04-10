package com.shark.feifei.db.cluster;

import com.shark.feifei.db.connection.FeifeiPoolDatasource;
import com.shark.feifei.db.cluster.env.Env;

import javax.sql.DataSource;

public class ClusterDataSource extends FeifeiPoolDatasource{
    private Env env;

    @Override
    public DataSource init() {
        this.setUrl(this.env.getUrl());
        this.setDriver(this.env.getDriver());
        this.setUsername(this.env.getUsername());
        this.setPassword(this.env.getPassword());
        return super.init();
    }

    public Env getEnv() {
        return env;
    }

    public ClusterDataSource setEnv(Env env) {
        this.env = env;
        return this;
    }
}
