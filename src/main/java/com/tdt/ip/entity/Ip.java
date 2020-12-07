package com.tdt.ip.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className Ip
 * @description
 * @date 2020-01-02 14:54
 **/
@Data
@Builder
@ToString
public class Ip implements Serializable {
    @Tolerate
    public Ip() {
    }

    private Integer id;
    private Long start;
    private Long end;
    private String state;
    private String province;
    private String city;
    private String level;
    private String network;
    private String from;
    private String to;
    private String gb;
    private Double lat;
    private Double lng;
}
