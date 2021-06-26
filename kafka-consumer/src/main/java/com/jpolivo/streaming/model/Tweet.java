package com.jpolivo.streaming.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "tweet")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tweet {
	@Id
	private String id;

	@Field(type = FieldType.Text, name = "content")
	private String content;
}
