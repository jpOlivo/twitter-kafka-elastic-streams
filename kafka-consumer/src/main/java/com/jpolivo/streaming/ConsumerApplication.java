package com.jpolivo.streaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumerApplication {

	// https://www.baeldung.com/spring-kafka
	// https://docs.spring.io/spring-kafka/reference/html/
	// https://bonsai.io
	// https://reflectoring.io/spring-boot-elasticsearch/
	// https://www.fatalerrors.org/a/concurrent-batch-processing-of-kafka-messages-in-spring-boot.html
	// https://codenotfound.com/spring-kafka-batch-listener-example.html
	
	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);

	}

}
