package com.springboot.reidslock.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Create by hyhweb on 2020/9/17 14:01
 */
@Component
public class RedisLock {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 加锁
     * @param key
     * @param value 当前时间+超时时间
     * @return
     */
    public boolean lock(String key, String value) {
        if (stringRedisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }
        //currentValue=A 这两个线程的value都是B 其中一个线程拿到锁
        String currentValue = stringRedisTemplate.opsForValue().get(key);
        //如果锁过期
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁的时间
            String oldValue = stringRedisTemplate.opsForValue().getAndSet(key, value);
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                return true;
            }
        }
        return false;
    }

    public void unlock(String key, String value) {
        String currentValue = stringRedisTemplate.opsForValue().get(key);
        try {
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                stringRedisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            System.out.println("【redis分布式锁】解锁异常");
            System.out.println(e.getStackTrace());
        }
    }
}
