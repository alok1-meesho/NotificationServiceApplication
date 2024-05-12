package com.example.notificationservice.controller;


import com.example.notificationservice.Repositories.SmsRequestRepository;
import com.example.notificationservice.entity.Exceptions.InvalidEntryException;
import com.example.notificationservice.entity.domain.SmsRequests;
import com.example.notificationservice.entity.dto.ErrorDetail;
import com.example.notificationservice.entity.dto.SmsApiRequest;
import com.example.notificationservice.entity.dto.SmsApiResponse;
import com.example.notificationservice.entity.events.SmsRequestKafkaEvent;
import com.example.notificationservice.handler.SmsRequestHandler;
import com.example.notificationservice.kafkaProducers.SmsRequestProducer;
import com.example.notificationservice.services.SmsRequestService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/sms")
@AllArgsConstructor
public class SmsRequestController {
    private static final Logger logger = LogManager.getLogger(SmsRequestController.class);

    private SmsRequestHandler smsRequestHandler;
    @PostMapping("/send")
    public ResponseEntity<?> sendSms(@RequestBody SmsApiRequest request) throws Exception  {
        logger.info("Sending SMS...");
        try{
            logger.info("SMS sent successfully.");
            return smsRequestHandler.sendSms(request);
        }
        catch (InvalidEntryException e){
            logger.error("Invalid request: {}", e.getMessage());
            throw e;
        }
        catch (Exception e){
            logger.error("Failed to send SMS", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{sms_Id}")
    public ResponseEntity<?> findById(@PathVariable int sms_Id)throws Exception{
        logger.info("Finding SMS by ID: {}", sms_Id);
        try{
            ResponseEntity<?> response=smsRequestHandler.findById(sms_Id);
            logger.info("SMS found: {}", response.getBody());
            return response;
        }
        catch (InvalidEntryException e){
            logger.error("Invalid request: {}", e.getMessage());
            throw e;
        }
        catch (Exception e){
            logger.error("Failed to find SMS by ID: {}", sms_Id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
