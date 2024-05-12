package com.example.notificationservice.services;


import com.example.notificationservice.Repositories.BlackListJpaRepository;
import com.example.notificationservice.Repositories.BlackListRepository;
import com.example.notificationservice.entity.domain.BlackListNumber;
import com.example.notificationservice.entity.dto.BlackListApiRequest;
import com.example.notificationservice.entity.dto.BlackListApiResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
// for defining all constructor
@AllArgsConstructor

public class BlackListService {
    private static final Logger logger = LoggerFactory.getLogger(BlackListService.class);
    BlackListRepository blackListRepository;
    BlackListJpaRepository blackListJpaRepository;
    public Set<String> findAll(){
        logger.info("Fetching all blacklisted numbers");
        return blackListRepository.findAll();
    }
    public BlackListApiResponse save(BlackListApiRequest blackListApiRequest){
        logger.info("Saving blacklisted numbers: {}", blackListApiRequest.getPhoneNumbers());

        for(String number:blackListApiRequest.getPhoneNumbers()){

            BlackListNumber blackListNumber= blackListJpaRepository.findByNumber(number);
            if(blackListNumber!=null){
                blackListNumber.setStatus(1);
            }else {
                blackListNumber = new BlackListNumber();
                blackListNumber.setNumber(number);
                blackListNumber.setStatus(1);
            }
            blackListJpaRepository.save(blackListNumber);
        }
        blackListRepository.save(blackListApiRequest.getPhoneNumbers());
        BlackListApiResponse blackListApiResponse= new BlackListApiResponse();
        blackListApiResponse.setData("Successfully blacklisted");
        logger.info("Blacklisted numbers saved successfully");
        return blackListApiResponse;
    }

    public BlackListApiResponse delete(BlackListApiRequest blackListApiRequest){
        logger.info("Deleting blacklisted numbers: {}", blackListApiRequest.getPhoneNumbers());
        blackListRepository.deleteNumber(blackListApiRequest.getPhoneNumbers());
        for(String number:blackListApiRequest.getPhoneNumbers()){

            BlackListNumber blackListNumber= blackListJpaRepository.findByNumber(number);
            if(blackListNumber!=null){
                blackListNumber.setStatus(0);
            }else {
                blackListNumber = new BlackListNumber();
                blackListNumber.setNumber(number);
                blackListNumber.setStatus(0);

            }
            blackListJpaRepository.save(blackListNumber);
        }

        BlackListApiResponse blackListApiResponse= new BlackListApiResponse();
        blackListApiResponse.setData("Successfully Whitelisted");
        logger.info("Blacklisted numbers deleted successfully");
        return blackListApiResponse;
    }

    public boolean isPresent(BlackListNumber blackListNumber){
        logger.info("Checking if number is present in blacklist: {}", blackListNumber.getNumber());
        return blackListRepository.isPresent(blackListNumber.getNumber());
    }





}
