package ru.shmvsky.springchat.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import ru.shmvsky.springchat.pojo.Message;

@Controller
public class ChatController {
	
	@MessageMapping("global-message")
	@SendTo("/topic/global-messages")
	public Message sendGlobalMessage(@Payload Message message, Principal principal) {
		message.setFrom(principal.getName());
		return message;
	}

}
