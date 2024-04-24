package com.example.notificationservice.Handler;


import com.example.notificationservice.entity.Exceptions.InvalidEntryException;
import com.example.notificationservice.entity.dto.BlackListApiRequest;
import com.example.notificationservice.entity.dto.BlackListApiResponse;
import com.example.notificationservice.entity.events.BlackListKafkaEvent;
import com.example.notificationservice.handler.BlackListHandler;
import com.example.notificationservice.kafkaProducers.BlackListProducer;
import com.example.notificationservice.services.BlackListService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@SpringBootTest
public class BlackListHandlerTest {

    @Autowired
    private BlackListHandler blackListHandler;
    @MockBean
    private BlackListService blackListService;
    @MockBean
    private BlackListProducer blackListProducer;

    @Test
    void testSave_ValidRequest() throws Exception {
        // Arrange
        BlackListApiRequest request = new BlackListApiRequest();
        request.setPhoneNumbers(List.of("123456789", "987654321"));

        BlackListKafkaEvent mockedEvent = new BlackListKafkaEvent();
        mockedEvent.setOperation("POST");

        BlackListApiResponse mockedResponse = new BlackListApiResponse();

        when(blackListProducer.sendMessage(any(BlackListKafkaEvent.class))).thenReturn(mockedResponse);

        // Act
        ResponseEntity<?> response = blackListHandler.save(request);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(mockedResponse, response.getBody());

        verify(blackListProducer, times(1)).sendMessage(any(BlackListKafkaEvent.class));
    }
    @Test
    void testSave_InvalidRequest() throws Exception {
        // Arrange
        BlackListApiRequest request = new BlackListApiRequest();

        InvalidEntryException exception =Assertions.assertThrows(InvalidEntryException.class,()-> blackListHandler.save(request));
    }
    @Test
    void testSave_InternalServerError() throws Exception {
        // Arrange
        BlackListApiRequest request = new BlackListApiRequest();
        request.setPhoneNumbers(List.of("123456789", "987654321"));

        when(blackListProducer.sendMessage(any(BlackListKafkaEvent.class))).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<?> response = blackListHandler.save(request);

        // Assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    @Test
    void testDelete_ValidRequest() throws Exception {
        // Arrange
        BlackListApiRequest request = new BlackListApiRequest();
        request.setPhoneNumbers(List.of("123456789", "987654321"));

        BlackListKafkaEvent mockedEvent = new BlackListKafkaEvent();
        mockedEvent.setOperation("DELETE");

        BlackListApiResponse mockedResponse = new BlackListApiResponse();

        when(blackListProducer.sendMessage(any(BlackListKafkaEvent.class))).thenReturn(mockedResponse);

        // Act
        ResponseEntity<?> response = blackListHandler.delete(request);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(mockedResponse, response.getBody());

        verify(blackListProducer, times(1)).sendMessage(any(BlackListKafkaEvent.class));
    }
    @Test
    void testDelete_InternalServerError() throws Exception {
        // Arrange
        BlackListApiRequest request = new BlackListApiRequest();
        request.setPhoneNumbers(List.of("123456789", "987654321"));

        when(blackListProducer.sendMessage(any(BlackListKafkaEvent.class))).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<?> response = blackListHandler.save(request);

        // Assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testFindAll_WithNonEmptySet() {
        // Arrange
        Set<String> mockBlackListNumbers = new HashSet<>();
        mockBlackListNumbers.add("123456789");
        when(blackListService.findAll()).thenReturn(mockBlackListNumbers);

        // Act
        ResponseEntity<?> response = blackListHandler.findAll();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(mockBlackListNumbers, response.getBody());
        verify(blackListService, times(1)).findAll();
    }
    @Test
    void testFindAll_WithEmptySet() {
        // Arrange
        when(blackListService.findAll()).thenReturn(new HashSet<>());

        // Act
        ResponseEntity<?> response = blackListHandler.findAll();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(Set.of(), response.getBody());
        verify(blackListService, times(1)).findAll();
    }
    @Test
    void testFindAll_ExceptionThrown() {
        // Arrange
        when(blackListService.findAll()).thenThrow(new RuntimeException("Test exception"));

        // Act
        ResponseEntity<?> response = blackListHandler.findAll();

        // Assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("error", response.getBody());
        verify(blackListService, times(1)).findAll();
    }



}
