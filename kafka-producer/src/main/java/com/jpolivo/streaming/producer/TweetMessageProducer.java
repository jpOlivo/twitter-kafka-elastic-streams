package com.jpolivo.streaming.producer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.extern.slf4j.Slf4j;
import twitter4j.Query;
import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;

@Component
@Slf4j
public class TweetMessageProducer implements MessageProducer {

	private static final String TWEETS_TOPIC_NAME = "tweets";
	private static final String SEARCH_TERM = "nasa OR covid OR bitcoin";

	private KafkaTemplate<String, String> kafkaTemplate;

	private Twitter twitter;

	private final AtomicInteger counter = new AtomicInteger();

	public TweetMessageProducer(KafkaTemplate<String, String> kafkaTemplate, Twitter twitter) {
		super();
		this.kafkaTemplate = kafkaTemplate;
		this.twitter = twitter;
	}

	@Override
	@Scheduled(fixedDelayString = "${scheduler.fixedDelay}")
	public void produce() {

		log.info("Starting iteration #{}", counter.incrementAndGet());

		Optional<RateLimitStatus> rateLimit = getTweetsRateLimit();
		rateLimit.ifPresent(s -> {
			log.info("You have {} calls remaining out of {}, Limit resets in {} seconds", s.getRemaining(),
					s.getLimit(), s.getSecondsUntilReset());

			// Do we need to delay because we've already hit our rate limits?
			if (s.getRemaining() == 0) {
				getInSleepMode(s);
			}
		});

		searchMostRecentTweets(SEARCH_TERM).forEach(t -> sendMessage(t));

		/*
		 * for (String tweet : searchMostRecentTweets(SEARCH_TERM)) {
		 * sendMessage(tweet); }
		 */

	}

	private void getInSleepMode(RateLimitStatus s) {
		try {
			int sleepDuration = s.getSecondsUntilReset() + 2;
			log.info("Sleeping for {} seconds due to rate limits", sleepDuration);
			TimeUnit.SECONDS.sleep(sleepDuration);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void sendMessage(String tweet) {
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(TWEETS_TOPIC_NAME, tweet);

		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

			@Override
			public void onSuccess(SendResult<String, String> result) {
				log.info("Sent message=[" + tweet + "] with offset=[" + result.getRecordMetadata().offset() + "]");
			}

			@Override
			public void onFailure(Throwable ex) {
				log.warn("Unable to send message=[" + tweet + "] due to : " + ex.getMessage());
			}
		});
	}

	private Optional<RateLimitStatus> getTweetsRateLimit() {
		RateLimitStatus rateLimitStatus = null;

		try {
			// This finds the rate limit specifically for doing the search API call we use
			rateLimitStatus = this.twitter.getRateLimitStatus("search").get("/search/tweets");

		} catch (TwitterException e) {
			log.warn("RateLimitStatus info is not available");
		}

		return Optional.ofNullable(rateLimitStatus);

	}

	private Iterable<String> searchMostRecentTweets(String q) {
		List<String> result = Collections.emptyList();
		try {
			result = this.twitter.search(new Query(q)).getTweets().stream().map(item -> item.getText())
					.collect(Collectors.toList());
		} catch (TwitterException e) {
			log.warn("There was an error to try get in the tweets");
		}

		return result;

	}

}
