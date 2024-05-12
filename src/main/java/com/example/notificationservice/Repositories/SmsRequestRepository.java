package com.example.notificationservice.Repositories;

import com.example.notificationservice.entity.domain.SmsRequests;
import org.springframework.data.repository.CrudRepository;

public interface SmsRequestRepository extends CrudRepository<SmsRequests,Integer> {

}
