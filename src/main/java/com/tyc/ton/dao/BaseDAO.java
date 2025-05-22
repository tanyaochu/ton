package com.tyc.ton.dao;

import com.tyc.ton.util.FormatUtil;
import com.tyc.ton.util.JDBCUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO {
    protected static final Logger logger = LogManager.getLogger(BaseDAO.class);
    /**
     * insert,update,delete generic method
     * @param sql sql
     * @param params args
     * @return affected rows
     * @throws SQLException exp
     */
    protected int update(String sql, Object... params) throws SQLException {
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }
        int rows = preparedStatement.executeUpdate();
        preparedStatement.close();
        if (connection.getAutoCommit()) {
            JDBCUtil.release();
        }
        return rows;
    }

    //genetic query method,多行多列List<Employee>，单行多列Employee，单行单列Double/Integer/String
    /**
     *
     * @param sql preparedSql
     * @param clazz tClazz
     * @param params args
     * @return list
     * @param <T> T
     * @throws Exception exp
     */
    protected <T> List<T> query(String sql, Class<T> clazz, Object... params) throws Exception {
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }

        List<T> list = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            T t = clazz.getDeclaredConstructor().newInstance();
            for (int i = 1; i <= columnCount; i++) {
                Object value = resultSet.getObject(i);
                String fieldName = metaData.getColumnLabel(i);
                //将数据库的下划线格式转换为驼峰格式
                String entityFieldName = FormatUtil.toCamelCase(fieldName);
                Field field = clazz.getDeclaredField(entityFieldName);
                field.setAccessible(true);
                field.set(t, value);
            }
            list.add(t);
        }
        resultSet.close();
        preparedStatement.close();
        if (connection.getAutoCommit()) {
            JDBCUtil.release();
        }
        return list;
    }

    protected <T> T singleQuery(String sql, Class<T> clazz, Object... params) throws Exception {
        List<T> list = query(sql, clazz, params);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.getFirst();
    }

}
