package com.shark.feifei.db;

import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Feifei data source
 * @Author: Shark Chili
 * @Date: 2018/9/3 0003
 */
public class FeifeiDatasource implements DataSource, XADataSource, Closeable, AutoCloseable {
	private static final org.slf4j.Logger LOGGER= LoggerFactory.getLogger(FeifeiDatasource.class);

	// base attribute
	private String driver;
	private String url;
	private String username;
	private String password;

	/**database name*/
	private String database;

	// pool and connection attribute
	private Integer connectionTimeout;

	@Override
	public Connection getConnection() throws SQLException {
		return getConnection(username,password);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		Connection connection=null;
		try {
			//1.加载驱动
			Class.forName(driver);
			//2.获取连接
			connection=DriverManager.getConnection(url,username,password);
			if (!connection.isClosed()){
				LOGGER.debug("Succeeded connecting to the database! connection: {}",connection);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}

	@Override
	public <T> T unwrap(Class<T> interfaceOrWrapper) throws SQLException {
		try {
			if (isWrapperFor(interfaceOrWrapper)) {
				return interfaceOrWrapper.cast(this);
			} else {
				throw new SQLException("The receiver is not a wrapper and does not implement the interface");
			}
		} catch (Exception e) {
			throw new SQLException("The receiver is not a wrapper and does not implement the interface");
		}
	}

	@Override
	public boolean isWrapperFor(Class<?> interfaceOrWrapper) throws SQLException {
		return interfaceOrWrapper.isInstance(this);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		connectionTimeout =seconds*1000;
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return connectionTimeout;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public XAConnection getXAConnection() throws SQLException {
		return null;
	}

	@Override
	public XAConnection getXAConnection(String user, String password) throws SQLException {
		return null;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
}
