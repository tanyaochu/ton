package com.tyc.ton.servlet;

import com.tyc.ton.dao.EmployeeDao;
import com.tyc.ton.dao.impl.EmployeeDaoImpl;
import com.tyc.ton.entity.Employee;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@WebServlet("/downloadTableServlet")
@MultipartConfig
public class DownloadServlet extends HttpServlet {
    static final Map<String, String> TABLES = new HashMap<>();
    private static final String employee =  "employee";
    private static final String department =  "department";
    static {
        TABLES.put("人事部员工信息表(点我)", "employee");
        TABLES.put("部门信息(这个还没搞好别点这个)", "department");
        // 添加其他表...
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取真实表数据
        String tableName = req.getParameter("tableName");
        String realTableName = TABLES.get(tableName);
        if(realTableName == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "表不存在");
            return;
        }
        //todo:这里怎么优化
        /*if(realTableName.equals(employee)) {
            EmployeeDao employeeDao = new EmployeeDaoImpl();
            List<Employee> employeeList = employeeDao.getAllEmployee();
        }*/
        /*if(realTableName.equals(department)) {
            departmentDao
        }*/
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        List<Employee> employeeList = employeeDao.getAllEmployee();
        // 生成 Excel 文件
        try (Workbook workbook = new XSSFWorkbook()) {  // 大数据量改用 SXSSFWorkbook
            Sheet sheet = workbook.createSheet("员工表");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "姓名", "性别", "地址"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 填充数据行
            int rowNum = 1;
            for (Employee employee : employeeList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(employee.getEmpId());
                row.createCell(1).setCellValue(employee.getEmpName());
                row.createCell(2).setCellValue(employee.getSex());
                row.createCell(3).setCellValue(employee.getAddress());
            }

            // 设置响应头，触发文件下载
            resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            resp.setHeader("Content-Disposition", "attachment; filename=employees.xlsx");

            // 将 Excel 写入响应流
            try (OutputStream outputStream = resp.getOutputStream()) {
                workbook.write(outputStream);
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "导出失败：" + e.getMessage());
        }

    }
}
