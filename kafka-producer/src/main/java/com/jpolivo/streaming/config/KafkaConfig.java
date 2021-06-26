package com.jpolivo.streaming.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	// Topics Configuration

	/*
	 * @Bean public KafkaAdmin kafkaAdmin() { Map<String, Object> configs = new
	 * HashMap<>(); configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
	 * bootstrapAddress); return new KafkaAdmin(configs); }
	 */

	@Bean
	public NewTopic topic1() {
		return TopicBuilder.name("tweets").partitions(3).replicas(1).build();
	}

	// Producers Configuration

	@Bean
	public ProducerFactory<String, String> simpleProducerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

		configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.SNAPPY.name);
		configProps.put(ProducerConfig.LINGER_MS_CONFIG, "20");
		configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024)); //32 KB batch size

		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}
}
