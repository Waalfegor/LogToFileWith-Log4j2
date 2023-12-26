package com.example.demo.components;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/** */
@Component
public class LoggingFilter extends OncePerRequestFilter {
    /** */
    private static final Logger LOGGER = LogManager.getLogger(LoggingFilter.class);

    /** */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (response.getCharacterEncoding() == null) {
            response.setCharacterEncoding("UTF-8"); // Or whatever default. UTF-8 is good for World Domination.
        }

        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier(response);

        long startTime = System.currentTimeMillis();

        filterChain.doFilter(request, responseCopier);
        responseCopier.flushBuffer();

        byte[] copy = responseCopier.getCopy();
        String responseBody = new String(copy);

        long duration = System.currentTimeMillis() - startTime;

        LOGGER.warn("request method: {}, request URI: {}, response status: {}, response body: {}," +
                        " request processing time: {} ms",
                request.getMethod(), request.getRequestURI(), response.getStatus(), responseBody, duration);
    }

}