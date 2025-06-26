package org.example.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Second var to create a request filter
//@Slf4j
//@Component
//public class RequestLoggingFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response,
//                                     FilterChain filterChain) throws ServletException, IOException {
//        log.info("Incoming request : " + request.getMethod() + " " +
//                request.getRequestURI());
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null) {
//            log.info("Authorization header : " + authHeader);
//        }
//        filterChain.doFilter(request, response);
//    }
//}
