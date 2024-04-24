package com.example.notificationservice.handler;


import com.example.notificationservice.adaptor.SmsServicesAdaptor;
import com.example.notificationservice.entity.Exceptions.InvalidEntryException;
import com.example.notificationservice.entity.domain.SmsRequests;
import com.example.notificationservice.entity.dto.ErrorDetail;
import com.example.notificationservice.entity.dto.SmsApiRequest;
import com.example.notificationservice.entity.dto.SmsApiResponse;
import com.example.notificationservice.entity.events.SmsRequestKafkaEvent;
import com.example.notificationservice.kafkaProducers.SmsRequestProducer;
import com.example.notificationservice.services.SmsRequestService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class SmsRequestHandler {
    private static final Logger logger = LogManager.getLogger(SmsRequestHandler.class);

    private SmsRequestService smsRequestService;
    private SmsRequestProducer smsRequestProducer;
    private SmsServicesAdaptor smsServicesAdaptor;
    public ResponseEntity<?> sendSms(SmsApiRequest request) throws Exception {
        logger.info("Sending SMS...");
        if (request.getNumber() == null || request.getNumber().isEmpty()) {
            logger.error("Invalid request: phone_number is mandatory");

            ErrorDetail errorDetail = new ErrorDetail(new ErrorDetail.ErrorInfo("Invalid_Request", "phone_number is mandatory"));
            throw new InvalidEntryException(errorDetail);
        } else {
            try {


                SmsRequests newSms = new SmsRequests();
                newSms.setNumber(request.getNumber());
                newSms.setMessage(request.getMessage());
                newSms.setStatus("In Progress");
                LocalDateTime currentTimestamp = LocalDateTime.now();
                newSms.setCreatedAt(currentTimestamp);
                newSms = smsRequestService.save(newSms);
                SmsRequestKafkaEvent event = smsServicesAdaptor.covertApiToEvent(newSms);
                SmsApiResponse response = smsRequestProducer.sendMessage(event);
                logger.info("SMS sent successfully.");
                return ResponseEntity.status(HttpStatus.valueOf(200)).body(response);
            } catch (Exception e) {
                logger.error("Failed to send SMS", e);
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

    }

    public ResponseEntity<?> findById(int id) throws  Exception{
        logger.info("Finding SMS by ID: {}", id);
        try {
            if (id <= 0) {
                logger.error("Invalid request: The Id must be greater than or equal to 0");
                ErrorDetail errorDetail=new ErrorDetail(new ErrorDetail.ErrorInfo("Invalid_Request", "The Id must be greater than equal to 0"));

                throw new InvalidEntryException(errorDetail);
            }
            SmsRequests sms = smsRequestService.findById(id);

            if (sms == null) {
                logger.warn("SMS with ID {} not found", id);
                ErrorDetail errorDetail=new ErrorDetail(new ErrorDetail.ErrorInfo("Invalid_Request", "The Id is Invalid"));

                throw new InvalidEntryException(errorDetail);
            }
            logger.info("SMS found: {}", sms);
            return ResponseEntity.ok(sms);
        }
        catch (InvalidEntryException e){
            logger.error("Invalid request: {}", e.getMessage());

            throw e;
        }
        catch (Exception e) {
            logger.error("Failed to find SMS by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
