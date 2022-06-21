package ru.shmvsky.springchat.service;

import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import ru.shmvsky.springchat.pojo.Message;

@Service
public class UserService {

	Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	public void sendPrivateMessage(Message message) {
		log.info("Sending private message");
		simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/topic/private-messages/", message);
	}

    @EventListener(SessionConnectEvent.class)
    public void handleWebsocketConnectListener(SessionConnectEvent event) {
        log.info(String.format("WebSocket connection established for sessionID %s",
                getSessionIdFromMessageHeaders(event)));
    }

    @EventListener(SessionDisconnectEvent.class)
    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event) {
        log.info(String.format("WebSocket connection closed for sessionID %s",
                getSessionIdFromMessageHeaders(event)));
    }

	private String getSessionIdFromMessageHeaders(SessionConnectEvent event) {
        Map<String, Object> headers = event.getMessage().getHeaders();
        return Objects.requireNonNull(headers.get("simpSessionId")).toString();
    }

	private String getSessionIdFromMessageHeaders(SessionDisconnectEvent event) {
        Map<String, Object> headers = event.getMessage().getHeaders();
        return Objects.requireNonNull(headers.get("simpSessionId")).toString();
    }

}
