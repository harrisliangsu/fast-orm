package com.shark.feifei.db.cluster.env;

import com.google.common.collect.Maps;

import java.util.Map;

public class EnvManager {

    // ClusterName, ClusterId, Env
    static Map<String,Map<String,Env>> ENV_MAP;

    public EnvManager addEnv(Env env) {
        checkNull();
        Map<String,Env> clusterEnv=ENV_MAP.get(env.getClusterName());
        if (clusterEnv==null){
            clusterEnv=Maps.newConcurrentMap();
            clusterEnv.put(env.getClusterId(),env);
            this.ENV_MAP.put(env.getClusterName(),clusterEnv);
        }else {
            clusterEnv.put(env.getClusterId(),env);
        }
        return this;
    }

    public Env getEnvRandom(String clusterName) {

        checkNull();
        return null;
    }

    public Env getEnvRandom(String clusterName,String clusterId) {
        return this.ENV_MAP.get(clusterName).get(clusterId);
    }

    private void checkNull() {
        if (ENV_MAP == null) {
            ENV_MAP = Maps.newConcurrentMap();
        }
    }
}
