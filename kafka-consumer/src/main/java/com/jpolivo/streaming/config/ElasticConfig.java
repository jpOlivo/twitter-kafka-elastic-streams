package com.jpolivo.streaming.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages 
        = "com.jpolivo.streaming.repositories")
public class ElasticConfig extends AbstractElasticsearchConfiguration {

	@Value(value = "${elasticsearch.hostAndPort}")
	private String hostAndPort;
	
	@Value(value = "${elasticsearch.accessKey}")
	private String accessKey;
	
	@Value(value = "${elasticsearch.secret}")
	private String secret;
	
	

	@Override
	@Bean
	public RestHighLevelClient elasticsearchClient() {
		final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
				.connectedTo(hostAndPort)
				.usingSsl()
				.withBasicAuth(accessKey, secret)   
				.build();

		return RestClients.create(clientConfiguration).rest();
	}

}
