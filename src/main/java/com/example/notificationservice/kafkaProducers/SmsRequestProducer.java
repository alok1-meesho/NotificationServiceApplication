package com.example.notificationservice.kafkaProducers;

import com.example.notificationservice.entity.dto.SmsApiRequest;
import com.example.notificationservice.entity.dto.SmsApiResponse;
import com.example.notificationservice.entity.events.SmsRequestKafkaEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class SmsRequestProducer {

    KafkaTemplate<SmsRequestKafkaEvent, String> kafkaTemplate;

    public SmsRequestProducer(KafkaTemplate<SmsRequestKafkaEvent, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public SmsApiResponse sendMessage(@RequestBody SmsRequestKafkaEvent event) {

        Message<SmsRequestKafkaEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "sms_request")
                .build();
        kafkaTemplate.send(message);
        SmsApiResponse response = new SmsApiResponse();
        response.setComment("everything is ok");
        response.setId(event.getRequestId());
        return response;
    }
}
