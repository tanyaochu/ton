package com.tyc.ton.servlet;

import com.tyc.ton.dao.LoginDao;
import com.tyc.ton.dao.impl.LoginDaoImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import com.tyc.ton.util.JwtUtil;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 从请求体读取 JSON 数据
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String jsonBody = sb.toString();

        // 解析 JSON
        JSONObject jsonResponse = new JSONObject(jsonBody);
        String username = jsonResponse.getString("username");
        String password = jsonResponse.getString("password");
        String token = JwtUtil.generateToken(username);
        LoginDao loginDaoImpl = new LoginDaoImpl();
        String queryPassword = loginDaoImpl.getPassword();

        //登陆密码验证
        if (password != null && queryPassword != null && password.equals(queryPassword)) {
            // 登录成功后生成 Token 并返回给前端
            jsonResponse.put("token", "Bearer " + token);
            resp.setHeader("token", "Bearer " + token);
            //密码正确，跳转index主页
            jsonResponse.put("success", true);
            jsonResponse.put("message", "登陆成功");
            jsonResponse.put("redirectUrl", "/static/html/index.html");
        } else {
            //失败信息返回前端
            jsonResponse.put("success", false);
            jsonResponse.put("message", "登陆失败");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 错误
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonResponse.toString());
    }
}
