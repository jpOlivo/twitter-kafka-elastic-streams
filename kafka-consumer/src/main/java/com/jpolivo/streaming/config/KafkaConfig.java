/*
package com.jpolivo.streaming.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
public class KafkaConfig {
	
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> batchFactory(Map<String, Object> config) {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(config));
		factory.setConcurrency(3); 		// In order to speed up consumption, I set concurrency to three (my topic has 3 partitions), that is, I have three Kafka message listener containers
		factory.setBatchListener(true); // In order to enable batch consumption.
		// factory.getContainerProperties().setPollTimeout(3000);
		return factory;
	}


	@Bean
	public Map<String, Object> consumerConfigs(KafkaProperties propsConfig) {
		Map<String, Object> propsMap = new HashMap<>();
		propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propsConfig.getBootstrapServers());
		propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, propsConfig.getConsumer().getEnableAutoCommit());
		propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, propsConfig.getConsumer().getGroupId());
		propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propsConfig.getConsumer().getAutoOffsetReset());
		propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5); // In order to set the maximum number of consumption records per batch.
		
		return propsMap;
	}
	
}
*/
