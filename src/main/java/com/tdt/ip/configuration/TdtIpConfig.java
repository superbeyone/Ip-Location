package com.tdt.ip.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className TdtIpConfig
 * @description 自定义配置类
 * @date 2020-01-02 13:50
 **/

@Data
@Component
@ConfigurationProperties(value = "tdt")
public class TdtIpConfig {
    private FileProperties fileProperties;

    private SearchProperties searchProperties;

    private RedisProperties redisProperties;

    private AdministrativeProperties administrativeProperties;
}
