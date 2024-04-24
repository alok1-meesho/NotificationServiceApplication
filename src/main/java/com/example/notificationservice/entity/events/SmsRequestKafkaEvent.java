package com.example.notificationservice.entity.events;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequestKafkaEvent {

    private int requestId;

}
