package com.example.notificationservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor

@Data
public class BlackListApiRequest {

    private List<String> phoneNumbers;


}
