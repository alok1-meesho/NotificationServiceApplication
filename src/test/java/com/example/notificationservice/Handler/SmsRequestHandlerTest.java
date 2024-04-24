package com.example.notificationservice.Handler;

import com.example.notificationservice.entity.Exceptions.InvalidEntryException;
import com.example.notificationservice.entity.domain.SmsRequests;
import com.example.notificationservice.entity.dto.SmsApiRequest;
import com.example.notificationservice.entity.dto.SmsApiResponse;
import com.example.notificationservice.entity.events.SmsRequestKafkaEvent;
import com.example.notificationservice.handler.SmsRequestHandler;
import com.example.notificationservice.kafkaProducers.SmsRequestProducer;
import com.example.notificationservice.services.SmsRequestService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@SpringBootTest
public class SmsRequestHandlerTest {
    @Autowired
    private SmsRequestHandler smsRequestHandler;
    @MockBean
    private SmsRequestService smsRequestService;
    @MockBean
    private SmsRequestProducer smsRequestProducer;
    @Test
    void testSendSms_WithValidRequest() throws Exception {
        // Arrange
        SmsApiRequest request = new SmsApiRequest();
        request.setNumber("123456789");
        request.setMessage("Test message");

        SmsRequests newSms = new SmsRequests();
        newSms.setId(1);
        newSms.setNumber(request.getNumber());
        newSms.setMessage(request.getMessage());
        newSms.setCreatedAt(LocalDateTime.now());
        newSms.setStatus("In Progress");

        SmsApiResponse mockedResponse = new SmsApiResponse();

        when(smsRequestService.save(any(SmsRequests.class))).thenReturn(newSms);
        when(smsRequestProducer.sendMessage(any(SmsRequestKafkaEvent.class))).thenReturn(mockedResponse);

        // Act
        ResponseEntity<?> response = smsRequestHandler.sendSms(request);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(mockedResponse, response.getBody());
    }

    @Test
    void testSendSms_WithInvalidRequest() throws Exception {

        SmsApiRequest request = new SmsApiRequest(); // Empty request
        InvalidEntryException exception = Assertions.assertThrows(InvalidEntryException.class,()->smsRequestHandler.sendSms(request));
        verify(smsRequestService,never()).save(any(SmsRequests.class));
        verify(smsRequestProducer,never()).sendMessage(any(SmsRequestKafkaEvent.class));
    }


    @Test
    void testSendSms_ExceptionThrown() throws Exception {
        // Arrange
        SmsApiRequest request = new SmsApiRequest();
        request.setNumber("123456789");
        request.setMessage("Test message");

        when(smsRequestService.save(any(SmsRequests.class))).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<?> response = smsRequestHandler.sendSms(request);

        // Assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testFindById_ValidId() throws Exception {
        // Arrange
        int validId = 1;
        SmsRequests mockedSms = new SmsRequests();
        mockedSms.setId(validId);

        when(smsRequestService.findById(validId)).thenReturn(mockedSms);

        // Act
        ResponseEntity<?> response = smsRequestHandler.findById(validId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(mockedSms, response.getBody());
        verify(smsRequestService, times(1)).findById(validId);
    }

    @Test
    void testFindById_InvalidId() throws Exception {
        // Arrange
        int invalidId = 0;

        // Act


        // Assert
        InvalidEntryException exception = Assertions.assertThrows(InvalidEntryException.class,()->smsRequestHandler.findById(invalidId));
        verifyNoInteractions(smsRequestService);
    }
    @Test
    void testFindById_SmsNotFound() {
        // Arrange
        int validId = 1;

        when(smsRequestService.findById(validId)).thenReturn(null);


        InvalidEntryException exception = Assertions.assertThrows(InvalidEntryException.class,()->smsRequestHandler.findById(validId));

    }
    @Test
    void testFindById_ExceptionThrown() throws Exception {
        // Arrange
        int validId = 1;

        when(smsRequestService.findById(validId)).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<?> response = smsRequestHandler.findById(validId);

        // Assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(smsRequestService, times(1)).findById(validId);
    }



}
