package com.example.notificationservice.entity.Exceptions;

import com.example.notificationservice.entity.dto.ErrorDetail;
import lombok.Getter;


@Getter
public class Message3rdPartyException extends Exception{
    private ErrorDetail error;
    public Message3rdPartyException(ErrorDetail error){
        this.error=error;
    }
}
