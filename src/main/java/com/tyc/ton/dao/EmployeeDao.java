package com.tyc.ton.dao;

import com.tyc.ton.entity.Employee;

import java.util.List;

public interface EmployeeDao {
   List<Employee> getAllEmployee();
   Employee getEmployeeByUsername(String username);
   Employee getEmployeeByUserId(String userId);
}
