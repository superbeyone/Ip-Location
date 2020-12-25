package com.tdt.ip.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className IpVo
 * @description
 * @date 2020-01-02 17:54
 **/
@Data
@Builder(toBuilder = true)
@ToString
public class IpVo implements Serializable {

    @Tolerate
    public IpVo() {
    }

    private String country;
    private String countryShort;
    private String countryEngFull;
    private String countryEngShort;

    private String province;
    private String provinceShort;
    private String provinceEngFull;
    private String provinceEngShort;

    private String city;
    private String cityShort;
    private String cityEngFull;
    private String cityEngShort;

    private String network;

    private String gb;

    private String lat;

    private String lng;

    private Integer level;

    /**
     * 洲
     */
    private String continent;


}
