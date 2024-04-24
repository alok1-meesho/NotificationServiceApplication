package com.example.notificationservice.entity.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
// It is used in elastic search database
@Document(indexName = "message_request")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor

@Data
public class ElasticSearchSchema {
    private int id;
    private String number;
    private String message;
    private Long time;
}
