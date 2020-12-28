package com.haulmont.testtask.dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {
    T selectOne(long id) throws SQLException;

    List<T> selectAll() throws SQLException;

    void insert(T t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(T t) throws SQLException;
}
