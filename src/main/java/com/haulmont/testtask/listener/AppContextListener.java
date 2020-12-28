package com.haulmont.testtask.listener;

import com.haulmont.testtask.util.DBManager;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try (Connection con = DBManager.getDataSource().getConnection()) {
            ScriptRunner sr = new ScriptRunner(con);
            Path path = Paths.get("src", "main", "resources", "sql", "init_db.sql");
            try (Reader reader = new BufferedReader(new FileReader(String.valueOf(path)))) {
                sr.runScript(reader);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            DBManager.getDataSource().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
