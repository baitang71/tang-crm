package com.cqupt.web.filter;

import com.cqupt.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        String path=request.getServletPath();
        if("/settings/user/login.do".equals(path)||"/login.jsp".equals(path)){
            filterChain.doFilter(request,response);
        }else{
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user != null) {
                filterChain.doFilter(request,response);
            }else{
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                System.out.println("======>重定向");
            }
        }
    }


    @Override
    public void destroy() {

    }
}
