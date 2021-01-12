package com.tdt.ip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author superbeyone
 */
@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication
public class IpServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpServiceApplication.class, args);
    }

}
