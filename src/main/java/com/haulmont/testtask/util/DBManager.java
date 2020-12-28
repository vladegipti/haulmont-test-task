package com.haulmont.testtask.util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DBManager {
    private static BasicDataSource dataSource;

    static {
        dataSource = new BasicDataSource();
        Path dbPath = Paths.get("db", "db");
        dataSource.setUrl("jdbc:hsqldb:file:" + dbPath + ";shutdown=true");
        dataSource.setUsername("SA");
        dataSource.setPassword("");

        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(20);
        dataSource.setMaxTotal(50);
    }

    public static BasicDataSource getDataSource() {
        return dataSource;
    }
}
