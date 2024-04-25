package com.example.notificationservice.services;


import com.example.notificationservice.Repositories.ElasticSearchRepository;
import com.example.notificationservice.entity.domain.ElasticSearchSchema;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElasticSearchService {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);

    private final ElasticSearchRepository elasticSearchRepository;
    public ElasticSearchService (ElasticSearchRepository elasticSearchRepository){
        this.elasticSearchRepository=elasticSearchRepository;
    }

    public List<ElasticSearchSchema> findMessage(Long start,Long end){

        try {
            logger.info("Finding messages between {} and {}", start, end);

            return elasticSearchRepository.findByTimeBetween(start, end);
        } catch (Exception e) {
            logger.error("Error finding messages between {} and {}: {}", start, end, e.getMessage());


            throw new RuntimeException("error in finding the message by TimeBetween : "+e.getMessage());
        }
    }

    public ElasticSearchSchema save(ElasticSearchSchema schema){
        try {
            logger.info("Saving data in Elasticsearch: {}", schema);

            return elasticSearchRepository.save(schema);
        } catch (Exception e) {
            logger.error("Error saving data in Elasticsearch: {}", e.getMessage());


            throw new RuntimeException("error in saving the data in elasticSearch : "+e.getMessage());
        }
    }


    public List<ElasticSearchSchema> findByMessage(String message){
        try {
            logger.info("Finding data by message: {}", message);

            return elasticSearchRepository.findByMessageContainingMessage(message);
        } catch (Exception e) {
            logger.error("Error finding data by message {}: {}", message, e.getMessage());

            throw new RuntimeException("error in finding the data by the message : "+e.getMessage());
        }
    }

}
