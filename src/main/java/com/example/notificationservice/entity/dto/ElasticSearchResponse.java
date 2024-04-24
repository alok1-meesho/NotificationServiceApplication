package com.example.notificationservice.entity.dto;

import com.example.notificationservice.entity.domain.ElasticSearchSchema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElasticSearchResponse {

    private int id;
    private String message;
    private String number;
    private LocalDateTime time;

}
