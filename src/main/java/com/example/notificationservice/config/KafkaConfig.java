package com.example.notificationservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public NewTopic JavaBuildTopic(){
        return TopicBuilder.name("sms_request")
                .build();
    }
}
