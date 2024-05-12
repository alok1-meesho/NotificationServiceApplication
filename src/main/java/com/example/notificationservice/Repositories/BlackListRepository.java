package com.example.notificationservice.Repositories;

import com.example.notificationservice.entity.domain.BlackListNumber;
import com.example.notificationservice.entity.dto.BlackListApiRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class BlackListRepository {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    public static final String HASH_KEY="BLNumber";
    public BlackListRepository(RedisTemplate<String,String> redisTemplate) {
        this.redisTemplate=redisTemplate;
    }
    public void save(List<String> blackListNumbers){
        for(String blackListNumber:blackListNumbers) {
            redisTemplate.opsForSet().add(HASH_KEY,blackListNumber);
        }
    }

    public Set<String> findAll(){
        Set<String> response=redisTemplate.opsForSet().members(HASH_KEY);
        return response;
    }

    public boolean isPresent(String number) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(HASH_KEY, number));
    }


    public void deleteNumber(List<String>blackListNumbers){

        for(String number:blackListNumbers) {
            redisTemplate.opsForSet().remove(HASH_KEY, number);
        }

    }
    public void deleteAll(){
        redisTemplate.opsForSet().remove(HASH_KEY);
    }

}
