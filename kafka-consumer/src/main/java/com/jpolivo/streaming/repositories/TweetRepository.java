package com.jpolivo.streaming.repositories;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.jpolivo.streaming.model.Tweet;

public interface TweetRepository extends ElasticsearchRepository<Tweet, String> {

	List<Tweet> findByContent(String content);

	List<Tweet> findByContentContaining(String term);

}
