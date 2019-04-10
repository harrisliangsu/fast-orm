package com.shark.feifei.db.cluster.env;

import java.util.List;

public class Env extends ConnectData {
    // id in cluster
    private String clusterId;
    // cluster name
    private String clusterName;
    // whether is cluster or not
    private boolean cluster;
    // whether is cluster master on mode of master-slave
    private boolean clusterMaster;
    // whether is cluster slave on mode of master-slave
    private boolean clusterSlave;
    // whether is cluster manager of partition
    private boolean clusterPartitionManager;
    // cluster mode
    private List<ClusterMode> clusterMode;

    public boolean isClusterPartitionManager() {
        return clusterPartitionManager;
    }

    public Env setClusterPartitionManager(boolean clusterPartitionManager) {
        this.clusterPartitionManager = clusterPartitionManager;
        return this;
    }

    public List<ClusterMode> getClusterMode() {
        return clusterMode;
    }

    public Env setClusterMode(List<ClusterMode> clusterMode) {
        this.clusterMode = clusterMode;
        return this;
    }

    public String getClusterName() {
        return clusterName;
    }

    public Env setClusterName(String clusterName) {
        this.clusterName = clusterName;
        return this;
    }

    public String getClusterId() {
        return clusterId;
    }

    public Env setClusterId(String clusterId) {
        this.clusterId = clusterId;
        return this;
    }

    public boolean isCluster() {
        return cluster;
    }

    public Env setCluster(boolean cluster) {
        this.cluster = cluster;
        return this;
    }

    public boolean isClusterMaster() {
        return clusterMaster;
    }

    public Env setClusterMaster(boolean clusterMaster) {
        this.clusterMaster = clusterMaster;
        return this;
    }

    public boolean isClusterSlave() {
        return clusterSlave;
    }

    public Env setClusterSlave(boolean clusterSlave) {
        this.clusterSlave = clusterSlave;
        return this;
    }
}
