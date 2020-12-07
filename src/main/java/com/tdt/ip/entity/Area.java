package com.tdt.ip.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className Region
 * @description
 * @date 2020-01-02 13:21
 **/

@Data
@ToString
public class Area implements Serializable {

    private Integer id;

    private String code;

    private String area;

    private String level;

    private String gb;
}
