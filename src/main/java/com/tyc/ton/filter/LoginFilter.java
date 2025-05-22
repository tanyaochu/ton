package com.tyc.ton.filter;

import com.tyc.ton.servlet.DownloadServlet;
import com.tyc.ton.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

//拦截所有请求
@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String uri = httpRequest.getRequestURI();

        // 登录请求和静态资源不需要拦截
        if (uri.startsWith("/static")
                || uri.endsWith(".html")
                || uri.endsWith(".css")
                || uri.endsWith(".js")
                || uri.equals("/loginServlet")) {
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }
        // 导航到登陆页面
        if (uri.equals("/") || uri.equals("/index") || uri.equals("/login")) {
            httpResponse.sendRedirect("/static/html/login.html");
            return;
        }
        //其他servlet请求需要认证token
        if (uri.endsWith("Servlet")) {
            String token = httpRequest.getHeader("token");
            if (token == null || !token.startsWith("Bearer ")) {
                httpResponse.sendError(401, "未提供 Token,请重新登录");
                return;
            }
            if (JwtUtil.validateToken(token.substring(7))) {
                filterChain.doFilter(httpRequest, httpResponse);
                return;
            }
        }
    }
}
