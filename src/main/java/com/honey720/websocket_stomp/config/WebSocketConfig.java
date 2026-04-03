package com.honey720.websocket_stomp.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * WebSocketConfig 클래스는 WebSocket 메시지 브로커를 설정하는 구성 클래스입니다.
 * @EnableWebSocketMessageBroker 어노테이션을 사용하여 WebSocket 메시지 브로커를 활성화하고,
 * WebSocketMessageBrokerConfigurer 인터페이스를 구현하여 WebSocket 엔드포인트와 메시지 브로커를 구성합니다.
 *
 * @author : honey720
 * @fileName : WebSocketConfig
 * @since : 2026-03-29
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Log log = LogFactory.getLog(WebSocketConfig.class);

    /**
     * registerStompEndpoints 메서드는 WebSocket 엔드포인트를 등록하는 메서드입니다.
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("[+] registerStompEndpoints called");
        registry
                // 클라이언트가 WebSocket 연결을 시도할 때 사용할 엔드포인트를 등록합니다. 예를 들어, "/ws-stomp" 엔드포인트를 등록합니다.
                .addEndpoint("/ws-stomp")
                // SockJS를 사용하여 WebSocket을 지원하지 않는 브라우저에서도 WebSocket과 유사한 기능을 사용할 수 있도록 합니다.
                .withSockJS(); // SockJS를 사용해야하는 상황인지 고려할 것.
    }

    /**
     * configureMessageBroker 메서드는 메시지 브로커를 구성하는 메서드입니다.
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        log.info("[+] configureMessageBroker called");
        registry
                // 클라이언트에서 메시지를 보낼 때 사용할 접두사를 설정합니다. 예를 들어, "/app"으로 시작하는 메시지는 @MessageMapping으로 처리됩니다.
                .setApplicationDestinationPrefixes("/app")
                // 메시지 브로커를 활성화하고, 클라이언트가 구독할 수 있는 접두사를 설정합니다. 예를 들어, "/topic"으로 시작하는 메시지는 구독할 수 있습니다.
                .enableSimpleBroker("/topic");
    }
}
