package ru.shmvsky.springchat;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import ru.shmvsky.springchat.pojo.UserPrincipal;

public class ConnectInterceptor implements ChannelInterceptor {
    
    private Logger log = LoggerFactory.getLogger(ConnectInterceptor.class);

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
 
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

            if (raw instanceof Map) {
                Object name = ((Map) raw).get("username");
 
                if (name instanceof ArrayList) {
                    accessor.setUser(new UserPrincipal(((ArrayList<String>) name).get(0).toString()));
                }
                
                log.info("New connection");

            }
        }

        return message;
	
    }

}
