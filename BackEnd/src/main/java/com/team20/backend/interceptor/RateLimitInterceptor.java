package com.team20.backend.interceptor;


import com.team20.backend.util.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final int LIMIT = 2; // 每秒允许的请求数
    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    @Resource
    private JwtUtils jwtUtils;
    private static final Logger logger = LoggerFactory.getLogger(RateLimitInterceptor.class);

    public RateLimitInterceptor() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::resetCounts, 0, 1, TimeUnit.SECONDS); // 每2秒重置一次计数器
    }

    private void resetCounts() {
        requestCounts.clear(); // 每次重置计数器时清空
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if (token != null && !token.isEmpty()) {
            String username = jwtUtils.getUsername(token);
            AtomicInteger count = requestCounts.computeIfAbsent(username, k -> new AtomicInteger(0));
            int currentCount = count.incrementAndGet();

            if (currentCount > LIMIT) {
                response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
                response.getWriter().write("TOO MANY REQUESTS");
                return false; // 拦截请求
            } else {
                logger.info("请求成功: 用户 {}", username);
            }
        }

        return true; // 继续处理请求
    }
}
