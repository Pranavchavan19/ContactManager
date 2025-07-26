package com.scm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws") // frontend will connect to this
                .setAllowedOriginPatterns("*")
                .withSockJS(); // fallback for older browsers
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // subscribers listen to /topic/...
        config.setApplicationDestinationPrefixes("/app"); // client sends to /app/...
    }
}
