package com.example.notificationservice.kafkaConsumers;


import com.example.notificationservice.entity.dto.BlackListApiResponse;
import com.example.notificationservice.entity.events.BlackListKafkaEvent;
import com.example.notificationservice.services.BlackListService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BlackListConsumer {
    private static final Logger logger = LoggerFactory.getLogger(BlackListConsumer.class);

    private BlackListService blackListService;
    public BlackListConsumer(BlackListService blackListService){
        this.blackListService=blackListService;
    }

    @KafkaListener(
            topics="blacklist_request",
            groupId= "myGroup"
    )

    public void consume(BlackListKafkaEvent blackListKafkaEvent) {
        try {
            if ("POST".equals(blackListKafkaEvent.getOperation())) {
                BlackListApiResponse blackListApiResponse = blackListService.save(blackListKafkaEvent.getBlackListApiRequest());
                logger.info("POST operation successfully processed: {}", blackListApiResponse);
            } else {
                BlackListApiResponse blackListApiResponse = blackListService.delete(blackListKafkaEvent.getBlackListApiRequest());
                logger.info("DELETE operation successfully processed: {}", blackListApiResponse);
            }
        } catch (Exception e) {
            logger.error("Error processing Kafka event: {}", blackListKafkaEvent, e);
            throw new RuntimeException(e);
        }
    }
}
