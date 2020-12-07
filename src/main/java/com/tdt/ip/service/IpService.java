package com.tdt.ip.service;

import com.tdt.ip.entity.IpVo;

import java.io.File;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className IpService
 * @description
 * @date 2020-01-02 15:12
 **/

public interface IpService {

    /**
     * 查询Ip数据
     *
     * @param ip       ip
     * @param regionDb db文件
     * @return 结果
     */
    IpVo getIpPosition(String ip, File regionDb) throws Exception;
}
