package com.jpolivo.streaming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.jpolivo.streaming.model.Tweet;
import com.jpolivo.streaming.repositories.TweetRepository;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
@TestMethodOrder(OrderAnnotation.class)
class TweetRepositoryIT {
	
	private static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:7.2.0";

	@Container
	private static ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer(ELASTICSEARCH_IMAGE);

	@Autowired
	private TweetRepository tweetRepository;

	@BeforeAll
	static void setUp() {
		System.setProperty("elasticsearch.hostAndPort", elasticsearchContainer.getHttpHostAddress());

		elasticsearchContainer.start();
	}

	@AfterAll
	static void destroy() {
		elasticsearchContainer.stop();
	}

	@Test
	@Order(1)
	void testSaveTweet() {
		Tweet expectedTweet = new Tweet("2", "My tweet content");
		Tweet actualTweet = tweetRepository.save(expectedTweet);
		assertNotNull(actualTweet);
		assertEquals(expectedTweet, actualTweet);

	}

	@Test
	@Order(2)
	void testSaveAllTweets() {
		List<Tweet> tweets = new ArrayList<Tweet>(
				Arrays.asList(new Tweet("3", "Another tweet"), new Tweet("4", "More tweets")));

		Iterable<Tweet> it = tweetRepository.saveAll(tweets);
		assertEquals(2, StreamSupport.stream(it.spliterator(), false).count());
	}

	/*
	 * @Test void testFindAllTweets() { tweetRepository.findAll().forEach(t ->
	 * System.out.println(t.getId() + " - " + t.getContent())); }
	 */

	@Test
	@Order(3)
	void testFindByContent() {
		List<Tweet> tweets = tweetRepository.findByContent("My tweet content");
		assertEquals(1, tweets.size());
	}

	@Test
	@Order(4)
	void testFindByContentContaining() {
		List<Tweet> tweets = tweetRepository.findByContentContaining("tweet");
		assertEquals(3, tweets.size());
	}

	@TestConfiguration
	static class ElasticTestContainersConfiguration extends AbstractElasticsearchConfiguration {

		@Bean
		@Override
		public RestHighLevelClient elasticsearchClient() {
			final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
					.connectedTo(elasticsearchContainer.getHttpHostAddress()).build();

			return RestClients.create(clientConfiguration).rest();

		}

	}

}