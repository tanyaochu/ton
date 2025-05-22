package com.tyc.ton.servlet;

import com.google.gson.Gson;
import com.tyc.ton.util.JwtUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/getTableServlet")
public class TableListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader("token");
        if (token == null || !token.startsWith("Bearer ")) {
            response.sendError(401, "未提供 Token");
            response.sendRedirect("/static/html/login.html");
        }
        if (JwtUtil.validateToken(token.substring(7))) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Gson gson = new Gson();
            String json = gson.toJson(DownloadServlet.TABLES.keySet());
            response.getWriter().write(json);
        }
    }
}
