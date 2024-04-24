package com.example.notificationservice.controller;


import com.example.notificationservice.entity.domain.ElasticSearchSchema;
import com.example.notificationservice.entity.dto.ElasticSearchRequest;
import com.example.notificationservice.entity.dto.ElasticSearchResponse;
import com.example.notificationservice.services.ElasticSearchService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@RestController
@ComponentScan
@RequestMapping("v1")
public class ElasticSearchController {
    private static final Logger logger = LogManager.getLogger(ElasticSearchController.class);

    private final ElasticSearchService elasticSearchService;

    public ElasticSearchController(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @GetMapping("/sms/between/time")
    public ResponseEntity<List<ElasticSearchResponse>> findBetweenTime(@RequestBody ElasticSearchRequest request) {
        try {
            Long start = Timestamp.valueOf(request.getStart()).getTime();
            Long end = Timestamp.valueOf(request.getEnd()).getTime();
            logger.info("Finding messages between {} and {}", request.getStart(), request.getEnd());
            List<ElasticSearchSchema> schemas = elasticSearchService.findMessage(start, end);
            List<ElasticSearchResponse> responses = new ArrayList<>();
            for (ElasticSearchSchema s : schemas) {
                ElasticSearchResponse response = new ElasticSearchResponse();
                response.setId(s.getId());
                response.setMessage(s.getMessage());
                response.setNumber(s.getNumber());
                Instant instant = Instant.ofEpochMilli(s.getTime());
                response.setTime(LocalDateTime.ofInstant(instant, ZoneId.of("UTC")));
                responses.add(response);
            }
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error occurred while finding messages between {} and {}: {}", request.getStart(), request.getEnd(), e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/find/message/{message}")
    public ResponseEntity<List<ElasticSearchResponse>> findByMessage(@PathVariable String message) {
        try {
            logger.info("Finding messages by message: {}", message);
            List<ElasticSearchSchema> schemas = elasticSearchService.findByMessage(message);
            List<ElasticSearchResponse> responses = new ArrayList<>();
            for (ElasticSearchSchema s : schemas) {
                ElasticSearchResponse response = new ElasticSearchResponse();
                response.setId(s.getId());
                response.setMessage(s.getMessage());
                response.setNumber(s.getNumber());
                Instant instant = Instant.ofEpochMilli(s.getTime());
                response.setTime(LocalDateTime.ofInstant(instant, ZoneId.of("UTC")));
                responses.add(response);
            }
            logger.debug("Found {} messages by message: {}", responses.size(), message);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error occurred while finding messages by message: {}", message, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
