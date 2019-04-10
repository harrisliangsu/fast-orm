package com.shark.feifei.db.cluster;

import com.google.common.collect.Maps;

import javax.sql.DataSource;
import java.util.Map;

public class ClusterDataSourceManger {

    static Map<String, DataSource> clusterDataSourceMap;

    public DataSource get(String clusterName){
        checkNull();
        return clusterDataSourceMap.get(clusterName);
    }

    public ClusterDataSourceManger addDataSource(ClusterDataSource dataSource){
        checkNull();
        clusterDataSourceMap.put(dataSource.getEnv().getClusterName(),dataSource);
        return this;
    }

    private void checkNull(){
        if (clusterDataSourceMap==null){
            clusterDataSourceMap= Maps.newConcurrentMap();
        }
    }

}
