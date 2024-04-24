package com.example.notificationservice.entity.Exceptions;


import com.example.notificationservice.entity.dto.ErrorDetail;
import lombok.Getter;

@Getter
public class InvalidEntryException extends  Exception{

    private ErrorDetail error;
    public InvalidEntryException(ErrorDetail error){
        this.error=error;
    }

}
