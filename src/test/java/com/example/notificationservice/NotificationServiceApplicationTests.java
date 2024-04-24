package com.example.notificationservice;

import com.example.notificationservice.Repositories.SmsRequestRepository;
import com.example.notificationservice.entity.domain.SmsRequests;
import com.example.notificationservice.services.SmsRequestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

@SpringBootTest
class NotificationServiceApplicationTests {

    @Autowired
    SmsRequestService smsRequestService;

    @MockBean
    SmsRequestRepository smsRequestRepository;

    @BeforeEach
    public void setup(){
        SmsRequests smsRequests= SmsRequests.builder().id(1).number("+918386999479").message("testing message")
                .failureCode(500).failureComment("").status("success").createdAt(LocalDateTime.of(1990, 5, 15, 9, 0,2,2)).updatedAt(LocalDateTime.of(1990, 5, 15, 10, 0,2,2)).build();
        Mockito.when(smsRequestRepository.save(smsRequests)).thenReturn(smsRequests);
    }
    @Test
    public void SaveSms(){
        SmsRequests smsRequests= SmsRequests.builder().id(1).number("+918386999479").message("testing message")
                .failureCode(500).failureComment("").status("success").createdAt(LocalDateTime.of(1990, 5, 15, 9, 0,2,2)).updatedAt(LocalDateTime.of(1990, 5, 15, 10, 0,2,2)).build();
        SmsRequests response = smsRequestService.save(smsRequests);
        Assertions.assertEquals(response.getNumber(),"+918386999479","failed Test Case");
    }




}
