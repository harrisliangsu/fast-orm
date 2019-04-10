package com.shark.feifei.consts;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/11/30 0030
 */
public class FeifeiConfigConst {
    public static final String CONFIG_FILE_DEFAULT = "application.properties";
    public static final String CONFIG_FILE_CHILD = "application-%s.properties";

    public static final String
            DB_URL = "db.url",
            DB_DRIVER = "db.driver",
            DB_USERNAME = "db.username",
            DB_PASSWORD = "db.password",
            DB_DATABASE = "db.database",
            DB_POOL_CONNECTION_TIMEOUT = "db.pool.connectionTimeout",
            DB_POOL_CONNECTION_MAX = "db.pool.connectionMax",
            DB_POOL_CONNECTION_INIT = "db.pool.connectionInit",
            DB_POOL_CONNECTION_IDLE_TIME = "db.pool.connectionIdleTime",
            DB_POOL_CONNECTION_ADD_NUM_ONCE_TIME = "db.pool.connectionAddNumOnceTime",
            DB_TRANSACTION_SCOPE = "db.transaction.scope",
            DB_ENV="db.env",
            DB_CLUSTER="db.cluster",
            DB_MASTER_MASTER="db.cluster.master",
            DB_CLUSTER_SLAVE="db.cluster.slave",
            DB_CLUSTER_NAME="db.cluster.name",
            DB_CLUSTER_ID="db.cluster.id",
            DB_CLUSTER_PARTITION_MANAGER="db.cluster.partition.manager";

    public static final String ENTITY_PACKAGE = "entity.package";

    public static final String DATABASE_TYPE = "queryConfig.databaseType";
    public static final String NAME_STYLE = "queryConfig.nameStyle";
    public static final String FIRE_TYPE = "cacheConfig.fireType";
}
