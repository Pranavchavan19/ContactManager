package com.scm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Register WebSocket endpoint for SockJS
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")         // WebSocket endpoint
                .setAllowedOriginPatterns("*")
                .withSockJS();              // Use SockJS fallback
    }

    // Configure message broker
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");         // Clients subscribe here
        registry.setApplicationDestinationPrefixes("/app"); // Client send here
    }
}
