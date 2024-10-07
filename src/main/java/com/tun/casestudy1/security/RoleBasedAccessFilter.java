package com.tun.casestudy1.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleBasedAccessFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String requestURI = httpRequest.getRequestURI();

        if (requestURI.startsWith("/login") || requestURI.startsWith("/css") || requestURI.startsWith("/js")) {
            chain.doFilter(request, response);
            return;
        }

        if (session == null || session.getAttribute("userRole") == null) {
            httpResponse.sendRedirect("/login");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");

        if (requestURI.startsWith("/admin") && !"ADMIN".equals(userRole)) {
            httpResponse.sendRedirect("/error1");
            return;
        }

        chain.doFilter(request, response);
    }
}
