package com.tdt.ip.utils;

import com.tdt.ip.configuration.TdtIpConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className RedisUtil
 * @description
 * @date 2020-01-08 16:25
 **/
@Component
public class RedisUtil {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    TdtIpConfig tdtIpConfig;


    


}
