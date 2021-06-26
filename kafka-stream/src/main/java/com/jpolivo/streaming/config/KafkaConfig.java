
package com.jpolivo.streaming.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableKafkaStreams
@Slf4j
public class KafkaConfig {
	private static final String INPUT_TOPIC = "tweets";
	private static final String OUTPUT_TOPIC = "tweets_uppercase";

	/*
	 * @Bean(name =
	 * KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME) public
	 * KafkaStreamsConfiguration kStreamsConfigs() { Map<String, Object> props = new
	 * HashMap<>(); props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
	 * "localhost:9092"); props.put(StreamsConfig.APPLICATION_ID_CONFIG,
	 * "myKafkaStreams"); props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
	 * Serdes.String().getClass().getName());
	 * props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
	 * Serdes.String().getClass().getName()); return new
	 * KafkaStreamsConfiguration(props); }
	 */

	@Bean
	public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
		KStream<String, String> stream = streamsBuilder.stream(INPUT_TOPIC,
				Consumed.with(Serdes.String(), Serdes.String()));

		// in topic -> transform -> out topic
		stream.map(this::uppercaseValue).to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));
		return stream;
	}

	private KeyValue<String, String> uppercaseValue(String key, String value) {
		String upperCase = value.toUpperCase();
		log.info("Transforming: [{}] -> [{}]", value, upperCase);
		
		return new KeyValue<>(key, upperCase);
	}
}
