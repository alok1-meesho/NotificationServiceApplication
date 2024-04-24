package com.example.notificationservice.entity.events;

import com.example.notificationservice.entity.dto.BlackListApiRequest;
import com.example.notificationservice.entity.dto.BlackListApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BlackListKafkaEvent {
    private  String operation;
    private BlackListApiRequest blackListApiRequest;

    public BlackListKafkaEvent(String operation){
        this.operation=operation;
    }


}
