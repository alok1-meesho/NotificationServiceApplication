package com.example.notificationservice.Repositories;

import com.example.notificationservice.entity.domain.BlackListNumber;
import com.example.notificationservice.entity.domain.SmsRequests;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListJpaRepository extends CrudRepository<BlackListNumber,Integer> {

    public BlackListNumber findByNumber(String number);
}
