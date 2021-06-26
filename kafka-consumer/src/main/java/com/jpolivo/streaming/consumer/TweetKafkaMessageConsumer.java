package com.jpolivo.streaming.consumer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.jpolivo.streaming.model.Tweet;
import com.jpolivo.streaming.repositories.TweetRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TweetKafkaMessageConsumer implements KafkaMessageConsumer {

	private static final String TWEETS_TOPIC_NAME = "tweets";
	private static final String GROUP_ID = "kafka-elastic-app";
	
	private TweetRepository tweetRepository;
	

	public TweetKafkaMessageConsumer(TweetRepository tweetRepository) {
		super();
		this.tweetRepository = tweetRepository;
	}

	
	/*
	@Override
	@KafkaListener(id = "myId", groupId = GROUP_ID, topics = TWEETS_TOPIC_NAME)
	public void consume(@Payload String in, 
						@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
						@Header(KafkaHeaders.OFFSET) long offset) {
		log.info("Received message [{}] from partition-{} with offset-{}", 
		        in, 
		        partition, 
		        offset);
		
		Tweet tweet = tweetRepository.save(new Tweet(buildTweetId(partition, offset), in));
		log.info("Data persisted on elasticseach: {} ", tweet.getId());
	}
	*/
	
	@Override
	@KafkaListener(id = "myId", groupId = GROUP_ID, topics = TWEETS_TOPIC_NAME, concurrency = "${spring.kafka.listener.concurrency:1}")
	public void consume(@Payload ConsumerRecords<String, String> records) {
		
		log.info("Received a batch of {} messages", records.count());
		
		List<Tweet> tweets = StreamSupport.stream(records.spliterator(), false)
			      .map(r -> new Tweet(buildTweetId(r.topic(), r.partition(), r.offset()), r.value()))
			      .collect(Collectors.toList());
		
		
		tweetRepository.saveAll(tweets);
		log.info("{} records were sent to elasticseach", tweets.size());
		
	}


	private String buildTweetId(String topic, int partition, long offset) {
		return String.join("_", topic, String.valueOf(partition), String.valueOf(offset));
	}

}
