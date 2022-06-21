package ru.shmvsky.springchat;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import ru.shmvsky.springchat.pojo.UserPrincipal;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {
		
		UUID id = UUID.randomUUID();
		UserPrincipal principal = new UserPrincipal(id.toString());

		return principal;
	}
}
