package com.tdt.ip.configuration;

import lombok.Data;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className RedisProperties
 * @description
 * @date 2020-01-08 16:29
 **/
@Data
public class RedisProperties {

    private long expireTime = 3600;

}
