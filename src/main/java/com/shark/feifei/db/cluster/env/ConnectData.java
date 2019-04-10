package com.shark.feifei.db.cluster.env;

public class ConnectData {
    private String driver;
    private String url;
    private String username;
    private String password;
    private String database;

    public String getDatabase() {
        return database;
    }

    public ConnectData setDatabase(String database) {
        this.database = database;
        return this;
    }

    public String getDriver() {
        return driver;
    }

    public ConnectData setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ConnectData setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ConnectData setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ConnectData setPassword(String password) {
        this.password = password;
        return this;
    }
}
