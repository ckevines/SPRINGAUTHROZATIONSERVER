package com.example.intermediateapp.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DuplicateRequestFilter implements Filter {

    // Map to store request fingerprints and their corresponding timestamps
    private final Map<String, Long> requestMap = new ConcurrentHashMap<>();

    @Value("${filter.requestCheckTime}")
    private int requestCheckTime;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        log.info("Duplicate Request: ({}) {}", requestMap.size() ,requestMap.toString());
        // Generate a unique fingerprint for the request
        String requestFingerprint = generateRequestFingerprint(httpRequest);

        // Check if the request is a duplicate
        if (isDuplicateRequest(requestFingerprint)) {
            httpResponse.sendError(HttpServletResponse.SC_CONFLICT, "Duplicate request detected");
            return;
        }
        chain.doFilter(request, response);

    }

    private String generateRequestFingerprint(HttpServletRequest request) {
        // You can customize this to include specific parameters or headers
        return request.getMethod() + ":" + request.getRequestURI() + ":" + request.getQueryString();
    }

    private boolean isDuplicateRequest(String requestFingerprint) {
        long currentTime = System.currentTimeMillis();
        Long lastRequestTime = requestMap.putIfAbsent(requestFingerprint, currentTime);

        if (lastRequestTime != null) {
            long requestInterval = currentTime - lastRequestTime;
            log.info("currentTime: ({}) ({})", currentTime, lastRequestTime);
            log.info("requestInterval: ({}) ", requestInterval);
            // Check if the request was made within the last 5 seconds (or your desired time window)
            if (requestInterval < requestCheckTime) {
                return true;
            } else {
                // Update the timestamp to the current time
                log.info("Update the timestamp to the current time: ({}) ({})", requestFingerprint, currentTime);
                requestMap.put(requestFingerprint, currentTime);
                log.info("requestMap {}", requestMap.toString());
            }
        }

        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}

