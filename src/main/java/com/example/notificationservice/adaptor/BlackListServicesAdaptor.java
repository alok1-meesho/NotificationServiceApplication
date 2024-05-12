package com.example.notificationservice.adaptor;


import com.example.notificationservice.entity.dto.BlackListApiRequest;
import com.example.notificationservice.entity.events.BlackListKafkaEvent;
import org.springframework.stereotype.Component;

@Component
public class BlackListServicesAdaptor {


    public BlackListKafkaEvent covertApiToEvent(BlackListApiRequest request,String Operation){
        BlackListKafkaEvent event = new BlackListKafkaEvent();
        event.setOperation(Operation);
        event.setBlackListApiRequest(request);
        return event;
    }
}
