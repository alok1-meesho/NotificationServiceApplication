package com.example.notificationservice.services;

import com.example.notificationservice.Repositories.SmsRequestRepository;
import com.example.notificationservice.entity.domain.SmsRequests;
import com.example.notificationservice.entity.dto.SmsApiRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SmsRequestService {

    private final SmsRequestRepository smsRequestRepository;
    private static final Logger logger = LoggerFactory.getLogger(SmsRequestService.class);

    public SmsRequests save(SmsRequests newSms){
        try {
            return smsRequestRepository.save(newSms);
        } catch (Exception e) {
            logger.error("Error saving SMS request: {}", e.getMessage());
            throw new RuntimeException("Error saving SMS request: " + e.getMessage());
        }
    }

    public SmsRequests findById(int id){
        try {
            Optional<SmsRequests> response = smsRequestRepository.findById(id);
            return response.orElse(null);
        } catch (Exception e) {
            logger.error("Error finding SMS request by ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error finding SMS request by ID " + id + ": " + e.getMessage());
        }
    }
}
