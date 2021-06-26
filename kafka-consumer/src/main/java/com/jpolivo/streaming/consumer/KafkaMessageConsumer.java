package com.jpolivo.streaming.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;

public interface KafkaMessageConsumer {
	//void consume(String in, int partition, long offset); // single

	void consume(ConsumerRecords<String, String> records); // batch
}
