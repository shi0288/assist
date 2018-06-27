package com.xyauto.assist.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class Cache {
    
    @Autowired  
    private StringRedisTemplate redisTemplate;

    public boolean set(final String key, String value) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value); 
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean set(final String key, String value, Long expireTime) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    public void removePattern(final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    public String get(final String key) {
        String result = redisTemplate.opsForValue().get(key);
        return result;
    }

    public void hmSet(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public String hmGet(String key, String hashKey) {
        return (String) redisTemplate.opsForHash().get(key, hashKey);
    }

    public void lPush(String k, String v) {
        redisTemplate.opsForList().rightPush(k, v);
    }

    public List<String> lRange(String k, long l, long l1) {
        return redisTemplate.opsForList().range(k, l, l1);
    }

    public void add(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public Set<String> setMembers(String key) { 
        return redisTemplate.opsForSet().members(key);
    }

    public void zAdd(String key, String value, double scoure) {
        redisTemplate.opsForZSet().add(key, value, scoure);
    }

    public Set<String> rangeByScore(String key, double scoure, double scoure1) {
        return redisTemplate.opsForZSet().rangeByScore(key, scoure, scoure1);
    }

    public int keys_count(final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        return keys.size();
    }

    public boolean setnx(String key, String value, Long seconds) {
        boolean ret = redisTemplate.opsForValue().setIfAbsent(key, value);
        if (ret) {
            if (seconds > 0) {
                return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            } else {
                return true;
            }
        }
        return false;
    }
}
