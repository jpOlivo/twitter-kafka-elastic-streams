package com.jpolivo.streaming.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class TwitterConfig {
	
	@Value(value = "${twitter4j.oauth.consumerKey}")
    private String consumerKey;
	
	@Value(value = "${twitter4j.oauth.consumerSecret}")
    private String consumerSecret;
	
	@Value(value = "${twitter4j.oauth.accessToken}")
    private String accessToken;
	
	@Value(value = "${twitter4j.oauth.accessTokenSecret}")
    private String accessTokenSecret;

	@Bean
	Twitter getTwitter() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken)
				.setOAuthAccessTokenSecret(accessTokenSecret);
		return new TwitterFactory(cb.build()).getInstance();
	}
}
