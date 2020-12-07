package com.tdt.ip.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className AdminData
 * @description
 * @date 2020-01-03 09:48
 **/
@Data
@Builder(toBuilder = true)
@ToString
public class AdminData implements Serializable {

    @Tolerate
    public AdminData() {
    }

    /**
     * 全名称
     */
    private String fullName;

    /**
     * 名称缩写
     */
    private String shortName;

    /**
     * 英文全称
     */
    private String engFullName;

    /**
     * 英文简称
     */
    private String engShortName;

    /**
     * 行政区划编码
     */
    private String gb;
    
    /**
     * 行政区划编码
     */
    private String gbCode;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 洲
     */
    private String continent;


}
