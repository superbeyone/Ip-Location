package com.tdt.ip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author superbeyone
 */
@EnableDiscoveryClient
@SpringBootApplication
public class IpServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpServiceApplication.class, args);
    }

}
