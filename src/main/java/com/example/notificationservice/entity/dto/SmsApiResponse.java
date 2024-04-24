package com.example.notificationservice.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class SmsApiResponse {
    private int id;
    private String comment;
}
