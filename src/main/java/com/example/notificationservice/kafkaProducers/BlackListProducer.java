package com.example.notificationservice.kafkaProducers;

import com.example.notificationservice.entity.dto.BlackListApiResponse;
import com.example.notificationservice.entity.events.BlackListKafkaEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class BlackListProducer {


    private KafkaTemplate<String, BlackListKafkaEvent> kafkaTemplate;
    public BlackListProducer(KafkaTemplate<String,BlackListKafkaEvent> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }


    public BlackListApiResponse sendMessage(BlackListKafkaEvent blackListKafkaEvent) {
        Message<BlackListKafkaEvent> message = MessageBuilder
                .withPayload(blackListKafkaEvent)
                .setHeader(KafkaHeaders.TOPIC, "blacklist_request")
                .build();
        kafkaTemplate.send(message);
        BlackListApiResponse response = new BlackListApiResponse();
        if(blackListKafkaEvent.getOperation().equals("POST")){
            response.setData("successfully Blacklisted");
        }else{
            response.setData("successfully Whitelisted");
        }
        return response;
    }


}
