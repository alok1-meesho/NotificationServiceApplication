package com.example.notificationservice.services;

import com.example.notificationservice.entity.Exceptions.Message3rdPartyException;
import com.example.notificationservice.entity.dto.ErrorDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

@Service
public class Message3rdPartyService {
    private static final Logger logger = LoggerFactory.getLogger(Message3rdPartyService.class);

    private final String apiUrl = "https://api.imiconnect.in/resources/v1/messaging";
    private final String accessToken = "c0c49ebf-ca44-11e9-9e4e-025282c394f2";

    private final RestTemplate restTemplate;

    public Message3rdPartyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> postDataToAPI(String textMessage, String msisdn, int correlationId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Key", accessToken);

            String requestBody = "{\"deliverychannel\": \"sms\", " +
                    "\"channels\": {\"sms\": {\"text\": \"" + textMessage + "\"}}, " +
                    "\"destination\": [{\"msisdn\": [\"" + msisdn + "\"], " +
                    "\"correlationId\": \"" + correlationId + "\"}]}";

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
            logger.info("Post request to API successful: {}", responseEntity.getBody());
            return responseEntity;
        } catch (Exception e) {
            logger.error("Error posting request to API: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
