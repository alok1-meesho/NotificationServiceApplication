package com.example.notificationservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Service;

@Getter @Setter
@AllArgsConstructor
public class ErrorDetail {
    ErrorInfo error;
    @Getter @Setter
    @AllArgsConstructor
    public static class ErrorInfo{
        String code;
        String comment;
    }
}
