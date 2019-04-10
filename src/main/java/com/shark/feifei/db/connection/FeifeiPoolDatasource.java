package com.shark.feifei.db.connection;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.shark.feifei.Exception.ConnectionException;
import com.shark.feifei.annoation.TaskClose;
import com.shark.feifei.data.KeyString;
import com.shark.feifei.db.task.TaskTime;
import com.shark.feifei.db.transaction.TransactionManager;
import com.shark.feifei.db.transaction.TransactionScope;
import com.shark.feifei.db.transaction.fire.FireTransaction;
import com.shark.job.job.ScheduleJob;
import com.shark.util.classes.ClassScannerUtil;
import com.shark.util.classes.ClassUtil;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

/**
 * A connection pool datasource
 *
 * @Author: Shark Chili
 * @Date: 2018/9/4 0004
 */
public class FeifeiPoolDatasource extends FeifeiDatasource implements Pool {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeifeiPoolDatasource.class);

    // base attribute
    /**
     * max num for pool hold connection
     */
    private Integer connectionMax;
    /**
     * num for pool created connection when it started
     */
    private Integer connectionInit;
    /**
     * max time for connection waiting for connection
     */
    private Integer connectionIdleTime;
    /**
     * add num when get no connection
     */
    private Integer connectionAddNumOnceTime;

    // pool attribute
    private Set<PoolConnection> allConnections;
    private BlockingQueue<PoolConnection> idleConnections;
    private Set<PoolConnection> activeConnections;
    private TransactionManager transactionManager;

    // lock
    private Lock lock = new ReentrantLock();

    // init jobs
    private List<ScheduleJob> initJobs;

    public FeifeiPoolDatasource() {
        variableInit();
    }

    public FeifeiPoolDatasource(TransactionScope scope, Integer connectionMax, Integer connectionInit, Integer connectionIdleTime, Integer connectionAddNumOnceTime) {
        this.transactionManager = new TransactionManager(scope);
        this.connectionMax = connectionMax;
        this.connectionInit = connectionInit;
        this.connectionIdleTime = connectionIdleTime;
        this.connectionAddNumOnceTime = connectionAddNumOnceTime;
        variableInit();
    }

    /**
     * Feifei pool datasource init
     *
     * @return this {@link FeifeiPoolDatasource}
     */
    public DataSource init() {
        // 已初始化则不再初始化
        if (allConnections.size() != 0) return this;
        // 创建 connectionInit 个数连接
        addConection(connectionInit);
        // 启动任务
        addInitJobs();
        return this;
    }

    /**
     * Start task
     */
    private void addInitJobs() {
        String packageName = TaskTime.class.getPackage().getName();
        LOGGER.info("Scan all job from package {}", packageName);
        //条件
        Predicate<Class<?>> predicate = aClass -> {
            boolean isActualClass = ClassScannerUtil.filterClass(aClass);
            boolean isTaskClass = ScheduleJob.class.isAssignableFrom(aClass);
            boolean notEnum = !aClass.isEnum();
            boolean isOpen = aClass.getAnnotation(TaskClose.class) == null;
            return isActualClass && isTaskClass && notEnum && isOpen;
        };

        Set<Class<?>> taskClasses = ClassScannerUtil.scanClassWithPredicate(packageName, predicate);
        for (Class<?> classes : taskClasses) {
            Class<? extends ScheduleJob> jobClass = (Class<? extends ScheduleJob>) classes;
            ScheduleJob scheduleJob = ClassUtil.newInstance(jobClass);
            assert scheduleJob != null;
            scheduleJob.getJobDetail().getJobDataMap().put(KeyString.DATASOURCE, this);
            // add jobs
            initJobs.add(scheduleJob);
        }
    }

    /**
     * Variable init
     */
    private void variableInit() {
        allConnections = Sets.newConcurrentHashSet();
        idleConnections = Queues.newPriorityBlockingQueue();
        activeConnections = Sets.newConcurrentHashSet();
        initJobs = Lists.newArrayList();
    }

    /**
     * add n connection to pool
     *
     * @param n the number of connection to add
     * @return set of {@link PoolConnection}
     */
    private Set<PoolConnection> addConection(int n) {
        if (n == 0) return null;
        Set<PoolConnection> poolConnections = new HashSet<>();
        for (int i = 0; i < n; i++) {
            try {
                PoolConnection poolConnection = new PoolConnection(super.getConnection());
                poolConnections.add(poolConnection);
                // 检查是否大于最大连接数
                if (i + 1 + allConnections.size() > connectionMax) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        allConnections.addAll(poolConnections);
        idleConnections.addAll(poolConnections);
        return poolConnections;
    }

    @Override
    public Connection getConnection() throws SQLException {
        // 根据连接有效范围决定是否使用共享连接
        Connection shareConnection = this.transactionManager.fireShareCon();
        if (shareConnection != null) {
            return shareConnection;
        }
        // 不使用共享连接
        lock.lock();
        boolean noIdleConnection = idleConnections.isEmpty();
        lock.unlock();
        if (noIdleConnection) {
            //增加连接
            boolean isFull = allConnections.size() >= connectionMax;
            int addNum = 0;
            if (isFull) {
                LOGGER.debug("Get connection failed,connection pool is full,and has no idle connection.");
                //加入当前线程到队列中,让当前线程等待
            } else if (allConnections.size() + connectionAddNumOnceTime > connectionMax) {
                addNum = allConnections.size() + connectionAddNumOnceTime - connectionMax;
            } else {
                addNum = connectionAddNumOnceTime;
            }
            addConection(addNum);
        }
        // 阻塞的获取一个连接
        try {
            lock.lock();
            PoolConnection poolConnection = useConnection(idleConnections.take());
            Connection connection = poolConnection.getConnection();
            // 获取连接后根据连接使用范围记录下连接
            transactionManager.recordCon(connection);
            lock.unlock();
            return connection;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Release connection by scope
     *
     * @param connection that will be released
     */
    public void releaseConnectionByScope(Connection connection) {
        this.transactionManager.recordCon(connection);
    }

    /**
     * to sue a connection
     *
     * @param poolConnection to be used
     * @return {@link PoolConnection} that had used
     */
    private PoolConnection useConnection(PoolConnection poolConnection) {
        poolConnection.use();
        idleConnections.remove(poolConnection);
        activeConnections.add(poolConnection);
        return poolConnection;
    }

    /**
     * to release a connection
     *
     * @param connection to release
     * @return {@link PoolConnection} contain the connection
     */
    public PoolConnection releaseConnection(Connection connection) {
        PoolConnection poolConnection = getPoolConnection(connection);
        poolConnection.release();
        idleConnections.add(poolConnection);
        activeConnections.remove(poolConnection);
        LOGGER.debug("release pool connection-{}", poolConnection);
        return poolConnection;
    }

    /**
     * Get a poolConnection by connection
     *
     * @param connection connection
     * @return {@link PoolConnection} contain the connection
     */
    private PoolConnection getPoolConnection(Connection connection) {
        for (PoolConnection poolConnection : allConnections) {
            if (poolConnection.getConnection().equals(connection)) {
                return poolConnection;
            }
        }
        throw new ConnectionException("not exist pool connection map to the connection");
    }

    /**
     * Check thread whether is alive or not
     */
    public void checkThreadIsAlive() {
        LOGGER.debug("start check thread alive");
    }

    /**
     * Check connection whether is valid or not
     */
    public void checkConnectionValid() {
        LOGGER.debug("start check connection valid");
        Set<PoolConnection> invalidConnection = new HashSet<>();
        for (PoolConnection poolConnection : allConnections) {
            try {
                Connection connection = poolConnection.getConnection();
                if (connection.isClosed()) {
                    invalidConnection.add(poolConnection);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //移除无效的连接
        lock.lock();
        LOGGER.debug("remove invalid pool connections {}", invalidConnection);
        allConnections.removeAll(invalidConnection);
        idleConnections.removeAll(invalidConnection);
        activeConnections.removeAll(invalidConnection);
        //添加新连接(移除多少添加多少)
        addConection(invalidConnection.size());
        lock.unlock();
    }

    public void sessionCommit() {
        FireTransaction sessionFT = this.transactionManager.sessionFireTransaction();
        if (sessionFT != null) {
            try {
                sessionFT.fireShareCon().commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Integer getConnectionMax() {
        return connectionMax;
    }

    public void setConnectionMax(Integer connectionMax) {
        this.connectionMax = connectionMax;
    }

    public Integer getConnectionInit() {
        return connectionInit;
    }

    public void setConnectionInit(Integer connectionInit) {
        this.connectionInit = connectionInit;
    }

    public Integer getConnectionIdleTime() {
        return connectionIdleTime;
    }

    public void setConnectionIdleTime(Integer connectionIdleTime) {
        this.connectionIdleTime = connectionIdleTime;
    }

    public Integer getConnectionAddNumOnceTime() {
        return connectionAddNumOnceTime;
    }

    public void setConnectionAddNumOnceTime(Integer connectionAddNumOnceTime) {
        this.connectionAddNumOnceTime = connectionAddNumOnceTime;
    }

    @Override
    public long getActiveNum() {
        return activeConnections.size();
    }

    @Override
    public long getIdleNum() {
        return idleConnections.size();
    }

    @Override
    public long getTotalNum() {
        return allConnections.size();
    }

    @Override
    public void setObjMaxNum(int maxNum) {
        setConnectionMax(maxNum);
    }

    @Override
    public void setObjInitNum(int initNum) {
        setConnectionInit(initNum);
    }

    public List<ScheduleJob> getInitJobs() {
        return initJobs;
    }
}
