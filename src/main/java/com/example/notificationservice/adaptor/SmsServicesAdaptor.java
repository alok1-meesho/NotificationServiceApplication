package com.example.notificationservice.adaptor;
import com.example.notificationservice.entity.domain.SmsRequests;
import com.example.notificationservice.entity.dto.SmsApiRequest;
import com.example.notificationservice.entity.events.SmsRequestKafkaEvent;
import org.springframework.stereotype.Component;

@Component
public class SmsServicesAdaptor {

    public SmsRequestKafkaEvent covertApiToEvent(SmsRequests requests){
        SmsRequestKafkaEvent event= new SmsRequestKafkaEvent(requests.getId());
        return event;
    }
}
