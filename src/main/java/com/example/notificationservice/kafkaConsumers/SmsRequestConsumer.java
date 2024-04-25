package com.example.notificationservice.kafkaConsumers;

import com.example.notificationservice.Repositories.BlackListRepository;
import com.example.notificationservice.entity.domain.ElasticSearchSchema;
import com.example.notificationservice.entity.domain.SmsRequests;
import com.example.notificationservice.entity.dto.ErrorDetail;
import com.example.notificationservice.entity.events.SmsRequestKafkaEvent;
import com.example.notificationservice.services.BlackListService;
import com.example.notificationservice.services.ElasticSearchService;
import com.example.notificationservice.services.Message3rdPartyService;
import com.example.notificationservice.services.SmsRequestService;
import com.example.notificationservice.utils.FunctionsAndConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.ValidationAnnotationUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SmsRequestConsumer {
    private static final Logger logger = LoggerFactory.getLogger(SmsRequestConsumer.class);
    private final BlackListRepository blackListRepository;
    private final SmsRequestService smsRequestService;
    private final ElasticSearchService elasticSearchService;
    private final FunctionsAndConditions functionsAndConditions;
    private final Message3rdPartyService message3rdPartyService;

    public SmsRequestConsumer(BlackListRepository blackListRepository,
                              SmsRequestService smsRequestService,
                              ElasticSearchService elasticSearchService,
                              FunctionsAndConditions functionsAndConditions,
                              Message3rdPartyService message3rdPartyService) {
        this.blackListRepository = blackListRepository;
        this.smsRequestService = smsRequestService;
        this.elasticSearchService = elasticSearchService;
        this.functionsAndConditions = functionsAndConditions;
        this.message3rdPartyService = message3rdPartyService;
    }

    @KafkaListener(
            topics = "sms_request",
            groupId = "myGroup"
    )
    public void consume(SmsRequestKafkaEvent smsRequestKafkaEvent) {
        try {
            SmsRequests message = smsRequestService.findById(smsRequestKafkaEvent.getRequestId());
            ElasticSearchSchema elasticSearchData = new ElasticSearchSchema();

            boolean validEvent = ValidateEvent(message);
            if(!validEvent){
                logger.info("validation in failed");
                smsRequestService.save(message);
                return;
            }
            logger.info("Validation is success");

            ResponseEntity<String> response = message3rdPartyService.postDataToAPI(message.getMessage(), message.getNumber(), message.getId());

            if (response.getStatusCode() == HttpStatus.OK) {
                functionsAndConditions.handleSuccessfulRequest(message, elasticSearchData);
                elasticSearchService.save(elasticSearchData);
                logger.info("SMS request processed successfully: {}", message.getId());


            } else {
                String errorMessage = "Error from 3rd party API: " + response.getBody();
                functionsAndConditions.handleApiFailure(message, errorMessage, response.getStatusCode().value());
                logger.error(errorMessage);
            }

            smsRequestService.save(message);
        } catch (Exception e) {
            logger.error("Error processing SMS request: {}", e.getMessage(), e);

            throw  new RuntimeException(e);
        }
    }

    private boolean ValidateEvent(SmsRequests message) {
        logger.info("Inside the ValidateEvent");

        if (!functionsAndConditions.validatePhoneNumber(message.getNumber())) {
            String errorMessage = "Phone Number is not valid: " + message.getNumber();
            functionsAndConditions.handleValidationFailure(message, errorMessage, 404);
            logger.warn(errorMessage);
            return false;
        } else if (blackListRepository.isPresent(message.getNumber())) {
            String errorMessage = "The Number is BlackListed: " + message.getNumber();
            functionsAndConditions.handleValidationFailure(message ,errorMessage, 404);
            logger.warn(errorMessage);
            return false;
        }
        return true;
    }


}
