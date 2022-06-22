package ru.shmvsky.springchat.controller;

import java.security.Principal;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import ru.shmvsky.springchat.pojo.Message;

@Controller
public class ChatController {

	private Logger log = LoggerFactory.getLogger(ChatController.class);

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@MessageMapping("/global-message")
	@SendTo("/topic/global-messages")
	public Message sendGlobalMessage(@Payload Message message, Principal principal) {
		String time = LocalTime.now().toString();
		String hoursAndMinutes = time.substring(0, 5);

		message.setTime(hoursAndMinutes);
		message.setFrom(principal.getName());

		log.info("New global message");

		return message;
	}

	@MessageMapping("/private-message")
	public void sendPrivateMessage(@Payload Message message, Principal principal) {	
		String time = LocalTime.now().toString();
		String hoursAndMinutes = time.substring(0, 5);
	
		message.setTime(hoursAndMinutes);
		message.setFrom(principal.getName());

		log.info("New private message");
	
		simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/queue/private-messages", message);
	}

}