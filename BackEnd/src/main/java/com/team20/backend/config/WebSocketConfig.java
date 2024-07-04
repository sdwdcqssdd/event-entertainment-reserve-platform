package com.team20.backend.config;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.team20.backend.util.JwtUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * AI-generated-content
 * tool: chatGpt
 * version: 3.5
 * usage: 让chatGpt给出了websocket配置的模版代码
 * 将配置本地化加入了token验证
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

        private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
        @Resource
        private JwtUtils jwtUtils;
        public WebSocketConfig() {
            logger.info("WebSocketConfig initialized");
        }
        @Override
        public void configureMessageBroker(MessageBrokerRegistry config) {
                config.enableSimpleBroker("/topic", "/queue");
                config.setApplicationDestinationPrefixes("/app");
                config.setUserDestinationPrefix("/user");
        }
        @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chatRoom")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        String query = request.getURI().getQuery();
                        if (query != null && query.startsWith("token=")) {
                            String token = query.substring(6);
                            if (jwtUtils.validateToken(token)) {
                                String username = jwtUtils.getUsername(token);
                                System.out.println(username+"加入了");
                                attributes.put("username", username);
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
                        // No implementation needed
                    }
                })
                .withSockJS();
    }
       
}
