package com.tdt.ip.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className Division
 * @description
 * @date 2020-01-02 18:00
 **/

@Data
@ToString
public class Division implements Serializable {

    private String name;

    private String gb;

    private String engName;

    private Double lng;

    private Double lat;


}
