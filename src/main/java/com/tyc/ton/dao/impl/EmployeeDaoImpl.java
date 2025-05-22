package com.tyc.ton.dao.impl;

import com.tyc.ton.dao.BaseDAO;
import com.tyc.ton.dao.EmployeeDao;
import com.tyc.ton.entity.Employee;

import java.util.List;

public class EmployeeDaoImpl extends BaseDAO implements EmployeeDao {

    @Override
    public List<Employee> getAllEmployee() {
        List<Employee> list = null;
        try {
            String sql = "select * from employee";
            list = query(sql, Employee.class,null);
            if(list != null && !list.isEmpty()) {
                return list;
            }else {
                logger.error("list is null");
            }
        } catch (Exception e) {
            logger.error("can not get table => employee");
        }
        return list;
    }

    @Override
    public Employee getEmployeeByUsername(String username) {
        return null;
    }

    @Override
    public Employee getEmployeeByUserId(String userId) {
        return null;
    }
}
