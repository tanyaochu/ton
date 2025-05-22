package com.tyc.ton.dao.impl;

import com.tyc.ton.dao.BaseDAO;
import com.tyc.ton.dao.LoginDao;


public class LoginDaoImpl extends BaseDAO implements LoginDao {

    public String getPassword() {
        String sql = "select password from auth";
        try {//todo:密码查询优化
            return "715";
        } catch (Exception e) {
            logger.error("select from table auth error {}", e.getMessage());
            return "404";
        }
    }
}
