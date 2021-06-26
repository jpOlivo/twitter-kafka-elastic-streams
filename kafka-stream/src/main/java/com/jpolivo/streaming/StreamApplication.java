package com.jpolivo.streaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StreamApplication {

	// https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.messaging.kafka.streams
	// https://mydeveloperplanet.com/2019/12/11/introduction-to-spring-kafka/
	
	public static void main(String[] args) {
		SpringApplication.run(StreamApplication.class, args);

	}

}
