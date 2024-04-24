package com.example.notificationservice.services;


import com.example.notificationservice.Repositories.BlackListJpaRepository;
import com.example.notificationservice.Repositories.BlackListRepository;
import com.example.notificationservice.entity.domain.BlackListNumber;
import com.example.notificationservice.entity.dto.BlackListApiRequest;
import com.example.notificationservice.entity.dto.BlackListApiResponse;
import com.example.notificationservice.services.BlackListService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class BlackListServiceTest {

    @Autowired
    private BlackListService blackListService;
    @MockBean
    private BlackListRepository blackListRepository;

    @MockBean
    private BlackListJpaRepository blackListJpaRepository;


    @Test
    public void FindAll(){
        Set<String> expectedNumbers = new HashSet<>();
        expectedNumbers.add("1234567890");
        expectedNumbers.add("0987654321");

        Mockito.when(blackListRepository.findAll()).thenReturn(expectedNumbers);

        // When
        Set<String> actualNumbers = blackListService.findAll();

        // Then
        Assertions.assertIterableEquals(expectedNumbers, actualNumbers);
    }
    @Test
    public void SaveBlacklistNumber(){
        BlackListApiRequest request= new BlackListApiRequest();
        request.setPhoneNumbers(Arrays.asList("+918239291325","+9112345467890"));

        Mockito.when(blackListJpaRepository.findByNumber("+918239291325")).thenReturn(null);
        Mockito.when(blackListJpaRepository.findByNumber("+9112345467890")).thenReturn(new BlackListNumber());

        BlackListApiResponse response = blackListService.save(request);
        Assertions.assertEquals(response.getData(),"Successfully blacklisted");
        Mockito.verify(blackListJpaRepository,Mockito.times(2)).save(Mockito.any(BlackListNumber.class));

    }
    @Test
    public void DeleteBlacklistNumber(){
        BlackListApiRequest request= new BlackListApiRequest();
        request.setPhoneNumbers(Arrays.asList("+918239291325","+9112345467890"));

        Mockito.when(blackListJpaRepository.findByNumber("+918239291325")).thenReturn(null);
        Mockito.when(blackListJpaRepository.findByNumber("+9112345467890")).thenReturn(new BlackListNumber());

        BlackListApiResponse response = blackListService.delete(request);
        Assertions.assertEquals(response.getData(),"Successfully Whitelisted");
        Mockito.verify(blackListJpaRepository,Mockito.times(2)).save(Mockito.any(BlackListNumber.class));

    }

    @Test
    public void IsPresent(){
        BlackListNumber blackListNumber = new BlackListNumber();
        Mockito.when(blackListRepository.isPresent(blackListNumber.getNumber())).thenReturn(true);

        Assertions.assertTrue(blackListService.isPresent(blackListNumber),"Is present Function is failed");

    }
}
