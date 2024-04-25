package com.example.notificationservice.Repositories;

import com.example.notificationservice.entity.domain.ElasticSearchSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElasticSearchRepository extends ElasticsearchRepository<ElasticSearchSchema, Integer> {


    public List<ElasticSearchSchema> findByTimeBetween(Long start,Long end);

    @Query("{\"match\": {\"message\": \"*?0*\"}}")
    List<ElasticSearchSchema> findByMessageContainingMessage(String searchTerm);

}
