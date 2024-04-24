package com.example.notificationservice.controller;


import com.example.notificationservice.entity.Exceptions.InvalidEntryException;
import com.example.notificationservice.entity.domain.BlackListNumber;
import com.example.notificationservice.entity.dto.BlackListApiRequest;
import com.example.notificationservice.entity.dto.BlackListApiResponse;
import com.example.notificationservice.entity.dto.ErrorDetail;
import com.example.notificationservice.entity.events.BlackListKafkaEvent;
import com.example.notificationservice.handler.BlackListHandler;
import com.example.notificationservice.kafkaProducers.BlackListProducer;
import com.example.notificationservice.services.BlackListService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class BlackListController {
    private BlackListService blackListService;
    private BlackListProducer blackListProducer;

    private BlackListHandler blackListHandler;
    @GetMapping("/blacklist")
    public ResponseEntity<?> findAll(){;
        try{
            return blackListHandler.findAll();
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/blacklist")
    public ResponseEntity<?> save(@RequestBody BlackListApiRequest blackListApiRequest) throws Exception{
        try {
            return blackListHandler.save(blackListApiRequest);
        }catch(InvalidEntryException e){
            System.out.println("InValidError at the Controller");
            throw e;
        }
        catch(Exception e){
            System.out.println("the error at the controller");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/blacklist")
    public ResponseEntity<?> delete(@RequestBody BlackListApiRequest blackListApiRequest) throws Exception{
        try {
            return blackListHandler.delete(blackListApiRequest);
        }catch(InvalidEntryException e){
            System.out.println("InValidError at the Controller");

            throw e;
        }
        catch(Exception e){
            System.out.println("the error at the controller");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
