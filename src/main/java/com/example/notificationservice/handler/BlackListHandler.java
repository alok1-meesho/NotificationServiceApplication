package com.example.notificationservice.handler;

import com.example.notificationservice.adaptor.BlackListServicesAdaptor;
import com.example.notificationservice.entity.Exceptions.InvalidEntryException;
import com.example.notificationservice.entity.dto.BlackListApiRequest;
import com.example.notificationservice.entity.dto.BlackListApiResponse;
import com.example.notificationservice.entity.dto.ErrorDetail;
import com.example.notificationservice.entity.events.BlackListKafkaEvent;
import com.example.notificationservice.kafkaProducers.BlackListProducer;
import com.example.notificationservice.services.BlackListService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Set;


@AllArgsConstructor
@Component
public class BlackListHandler {
    private static final Logger logger = LoggerFactory.getLogger(BlackListHandler.class);
    private BlackListProducer blackListProducer;
    private BlackListService blackListService;
    private BlackListServicesAdaptor blackListServicesAdaptor;
    public ResponseEntity<?> save(BlackListApiRequest blackListApiRequest) throws Exception{
        logger.info("Processing request to save blacklisted numbers...");

        if (blackListApiRequest!=null&&blackListApiRequest.getPhoneNumbers()!=null&&!blackListApiRequest.getPhoneNumbers().isEmpty()) {
            try {
                BlackListKafkaEvent event = blackListServicesAdaptor.covertApiToEvent(blackListApiRequest,"POST");
                BlackListApiResponse response = blackListProducer.sendMessage(event);
                logger.info("Successfully sent blacklisted numbers to Kafka producer.");
                return ResponseEntity.ok(response);
            }catch(Exception e){
                logger.info("Successfully sent blacklisted numbers to Kafka producer.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            String errorMessage = "Invalid request: You have not provided a phoneNumber List";
            logger.error(errorMessage);
            ErrorDetail errorDetail=new ErrorDetail(new ErrorDetail.ErrorInfo("Invalid_Request", errorMessage));
            throw new InvalidEntryException(errorDetail);
        }
    }
    public ResponseEntity<?> findAll(){
        logger.info("Processing request to retrieve all blacklisted numbers...");

        try {

            Set<String> blackListNumbers = blackListService.findAll();

            if (!blackListNumbers.isEmpty()) {
                logger.info("Retrieved {} blacklisted numbers.", blackListNumbers.size());

                return ResponseEntity.ok(blackListNumbers);
            } else {
                logger.info("No blacklisted numbers found.");

                return ResponseEntity.ok(Set.of());
            }
        } catch (Exception e) {
            logger.error("Error while retrieving all blacklisted numbers: {}", e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }


    public ResponseEntity<?> delete(BlackListApiRequest blackListApiRequest) throws  Exception{
        logger.info("Processing request to delete blacklisted numbers...");

        if (blackListApiRequest!=null&&!blackListApiRequest.getPhoneNumbers().isEmpty()) {
            try {
                BlackListKafkaEvent event = blackListServicesAdaptor.covertApiToEvent(blackListApiRequest,"DELETE");
                BlackListApiResponse response = blackListProducer.sendMessage(event);
                logger.info("Successfully sent delete request to Kafka producer.");
                return ResponseEntity.ok(response);
            }catch(Exception e){
                logger.error("Error while deleting blacklisted numbers: {}", e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            String errorMessage = "Invalid request: You have not provided a phoneNumber List";
            logger.error(errorMessage);
            ErrorDetail errorDetail=new ErrorDetail(new ErrorDetail.ErrorInfo("Invalid_Request", errorMessage));
            throw new InvalidEntryException(errorDetail);
        }
    }


}
