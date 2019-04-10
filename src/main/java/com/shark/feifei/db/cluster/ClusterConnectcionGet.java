package com.shark.feifei.db.cluster;

import com.mchange.v2.util.PropertiesUtils;
import com.shark.feifei.consts.FeifeiConfigConst;
import com.shark.feifei.consts.FeifeiConfigDefault;
import com.shark.feifei.db.cluster.env.Env;
import com.shark.feifei.db.cluster.env.EnvManager;
import com.shark.feifei.query.connection.AbstractConnectionGet;
import com.shark.feifei.query.connection.ConnectionGet;
import com.shark.job.job.ScheduleJob;
import com.shark.util.util.FileUtil;
import com.shark.util.util.PropertyUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ClusterConnectcionGet extends AbstractConnectionGet{

    @Override
    public void initDataSource() {
        Properties properties= FileUtil.readProperties("/"+ FeifeiConfigConst.CONFIG_FILE_DEFAULT);
        String env= (String) PropertyUtil.getOrDefault(properties,FeifeiConfigConst.DB_ENV, FeifeiConfigDefault.DB_ENV);
        String configFileName="application-"+env+".properties";
        Properties dbProperties=FileUtil.readProperties("/"+configFileName);

    }

    private void readEnv(Properties properties){
        String driver= (String) properties.get(FeifeiConfigConst.DB_DRIVER);
        String cluster= (String) PropertyUtil.getOrDefault(properties,FeifeiConfigConst.DB_CLUSTER,FeifeiConfigDefault.DB_CLUSTER);
        String clusterName= (String) PropertyUtil.getOrDefault(properties,FeifeiConfigConst.DB_CLUSTER_NAME,null);

        boolean isCluster=cluster.equals("true");
        if (!isCluster){
            Env env=new Env().setCluster(false);
            String url=(String) properties.get(FeifeiConfigConst.DB_URL);
            String username=(String) properties.get(FeifeiConfigConst.DB_USERNAME);
            String password=(String) properties.get(FeifeiConfigConst.DB_PASSWORD);
            String database=(String) properties.get(FeifeiConfigConst.DB_DATABASE);
            env.setUrl(url).setDriver(driver).setUsername(username).setPassword(password).setDatabase(database);
        }
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key= (String) entry.getKey();
            String value= (String) entry.getValue();
            if (key.startsWith(FeifeiConfigConst.DB_URL)){

            }else if (key.startsWith(FeifeiConfigConst.DB_USERNAME)){

            }else if (key.startsWith(FeifeiConfigConst.DB_PASSWORD)){

            }else if (key.startsWith(FeifeiConfigConst.DB_PASSWORD)){

            }
        }
    }

    @Override
    public void release(Connection connection) {

    }

    @Override
    public List<ScheduleJob> initJobs() {
        return null;
    }

    @Override
    public void sessionCommit() {

    }
}
