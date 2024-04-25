package com.example.notificationservice.utils;


import com.example.notificationservice.entity.domain.ElasticSearchSchema;
import com.example.notificationservice.entity.domain.SmsRequests;
import com.example.notificationservice.kafkaConsumers.SmsRequestConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class FunctionsAndConditions {

    private static final Logger logger = LoggerFactory.getLogger(FunctionsAndConditions.class);
    public boolean validatePhoneNumber(String number){

        if(number==null||number.length()!=13||number.charAt(0)!='+'||number.charAt(1)!='9'||number.charAt(2)!='1'){
            logger.info("Initial condition of phoneNumber is not valid");
            return false;
        }
        for(int i=3;i<number.length();i++){
            if(number.charAt(i)<'0'||number.charAt(i)>'9'){
                logger.info("There is some other character present in the phoneNumber rather than numerical");
                return false;
            }
        }
        logger.info("phoneNumber is correct");
        return true;
    }
    public void handleValidationFailure(SmsRequests message, String failureComment, int failureCode) {
        logger.info("handleValidationFailure in the FunctionAndValidation class");
        message.setStatus("failed");
        message.setFailureComment(failureComment);
        message.setFailureCode(failureCode);
    }
    public void handleSuccessfulRequest(SmsRequests message, ElasticSearchSchema elasticSearchData) {
        logger.info("handleSuccessfulRequest in the FunctionAndValidation class");
        message.setStatus("success");
        elasticSearchData.setId(message.getId());
        elasticSearchData.setMessage(message.getMessage());
        elasticSearchData.setNumber(message.getNumber());
        LocalDateTime curr = LocalDateTime.now();
        message.setUpdatedAt(curr);
        Long time = Timestamp.valueOf(curr).getTime();
        elasticSearchData.setTime(time);
    }

    public void handleApiFailure(SmsRequests message, String failureComment, int failureCode) {
        message.setStatus("failed");
        message.setFailureComment(failureComment);
        message.setFailureCode(failureCode);
    }
}
