package com.tdt.ip.configuration;

import lombok.Data;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className FileProperties
 * @description 文件配置
 * @date 2020-01-02 13:51
 **/
@Data
public class FileProperties {

    private String regionCsv;

    private String ipTxt;

    private String regionDb;

    private String adminDataCsv;
}
