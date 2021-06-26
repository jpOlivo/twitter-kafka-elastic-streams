package com.jpolivo.streaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProducerApplication {

	// https://www.socialseer.com/twitter-programming-in-java-with-twitter4j/how-to-retrieve-more-than-100-tweets-with-the-twitter-api-and-twitter4j/
	// https://www.baeldung.com/twitter4j
	// https://www.baeldung.com/spring-kafka
	// https://docs.spring.io/spring-kafka/reference/html/
	// https://medium.com/@TimvanBaarsen/programmatically-create-kafka-topics-using-spring-kafka-8db5925ed2b1

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);

	}

}
