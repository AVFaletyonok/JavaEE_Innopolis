package org.example.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@RestControllerAdvice
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        var base64Decoder = Base64.getDecoder();

        String encodedHeader = request.getHeader("Authorization");
        String decodedHeader = "null";
        if (encodedHeader != null) {
            decodedHeader = new String(base64Decoder.decode(encodedHeader.replace("Basic ", "")));
        }

        log.info("Input request: {} {}, Authorization Header {}",
                request.getMethod(), request.getRequestURI(), decodedHeader);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
