package ru.shmvsky.springchat.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Message {
	private String from;
	private String to;
	private String content;
	private String time;
}
