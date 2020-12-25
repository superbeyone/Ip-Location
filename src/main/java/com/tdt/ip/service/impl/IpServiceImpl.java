package com.tdt.ip.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.HashMultimap;
import com.tdt.ip.commons.DataInitBean;
import com.tdt.ip.configuration.TdtIpConfig;
import com.tdt.ip.entity.AdminData;
import com.tdt.ip.entity.IpVo;
import com.tdt.ip.service.IpService;
import com.tdt.ip.utils.DbSearcher;
import com.tdt.ip.utils.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className IpServiceImpl
 * @description
 * @date 2020-01-02 15:12
 **/
@Service
public class IpServiceImpl implements IpService {

    @Autowired
    DataInitBean dataInitBean;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    TdtIpConfig tdtIpConfig;

    private Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 查询Ip数据
     *
     * @param ip       ip
     * @param regionDb db文件
     * @return 结果
     */
    @Override
    public IpVo getIpPosition(String ip, File regionDb) throws Exception {

        if (!IpUtil.isIp(ip)) {
            return null;
        }
        Object result = get(IpUtil.convertKey(ip));
        if (result != null && result instanceof String) {
            return JSON.parseObject((String) result, IpVo.class);
        }
        DbSearcher searcher = dataInitBean.getDbSearcher();

        //define the method
        Method method = dataInitBean.getMethod();
        Object invoke = method.invoke(searcher, ip);
        if (invoke instanceof IpVo) {
            IpVo ipVo = convertIp((IpVo) invoke);

            ipVo = setIpLevel(ipVo);
            set(IpUtil.convertKey(ip), JSON.toJSONString(ipVo));
            return ipVo;
        }
        return null;
    }

    private IpVo setIpLevel(IpVo ipVo) {
        String gb = ipVo.getGb();
        Integer level = dataInitBean.getGlobalGbAndLevelMap().get(gb);
        ipVo.setLevel(level);
        return ipVo;
    }


    /**
     * 添加到redis
     *
     * @param key key
     * @param val 值
     * @return 结果
     */
    private boolean set(String key, Object val) {
        try {
            redisTemplate.opsForValue().set(key, val, tdtIpConfig.getRedisProperties().getExpireTime(), TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加到 redis
     *
     * @param key key
     * @return 值
     */
    private Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }


    private IpVo convertIp(IpVo ipVo) {
        if (ipVo == null) {
            return null;
        }
        String country = convertStr(ipVo.getCountry());
        ipVo.setCountry(country);
        String province = convertStr(ipVo.getProvince());
        ipVo.setProvince(province);
        String city = convertStr(ipVo.getCity());
        ipVo.setCity(city);
        String network = convertStr(ipVo.getNetwork());
        ipVo.setNetwork(network);

        HashMultimap<String, AdminData> fullNameAdminDataMap = dataInitBean.getFullNameAdminDataMap();
        HashMultimap<String, AdminData> shortNameAdminDataMap = dataInitBean.getShortNameAdminDataMap();

        Set<AdminData> adminDataSet;
        if (StringUtils.isNotBlank(country)) {
            adminDataSet = shortNameAdminDataMap.get(country);
            if (adminDataSet == null || adminDataSet.size() == 0) {
                adminDataSet = fullNameAdminDataMap.get(country);
            }
            if (adminDataSet != null && adminDataSet.size() > 0) {
                logger.debug("根据国家级名称\t{},查询结果数为:\t{}", country, adminDataSet.size());
                for (AdminData adminData : adminDataSet) {
                    ipVo.setCountry(adminData.getFullName());
                    ipVo.setCountryShort(adminData.getShortName());
                    ipVo.setCountryEngFull(adminData.getEngFullName());
                    ipVo.setCountryEngShort(adminData.getEngShortName());
                    resetCommonField(ipVo, adminData);
                }
            }

        }

        if (StringUtils.isNotBlank(province)) {
            adminDataSet = fullNameAdminDataMap.get(province);
            if (adminDataSet == null || adminDataSet.size() == 0) {
                adminDataSet = shortNameAdminDataMap.get(province);
            }
            if (adminDataSet != null && adminDataSet.size() > 0) {
                String gb = "";
                if (StringUtils.isNotBlank(ipVo.getGb()) && adminDataSet.size() > 1) {
                    gb = StringUtils.substring(ipVo.getGb(), 0, Math.min(5, ipVo.getGb().length()));
                }
                logger.debug("根据省级名称\t{},查询结果数为:\t{}", province, adminDataSet.size());
                for (AdminData adminData : adminDataSet) {
                    if (adminDataSet.size() > 1) {
                        if (StringUtils.isNotBlank(ipVo.getGb())) {
                            ipVo.setProvince(adminData.getFullName());
                            ipVo.setProvinceShort(adminData.getShortName());
                            ipVo.setProvinceEngFull(adminData.getEngFullName());
                            ipVo.setProvinceEngShort(adminData.getEngShortName());
                            resetCommonField(ipVo, adminData);
                            break;
                        }
                    } else {
                        ipVo.setProvince(adminData.getFullName());
                        ipVo.setProvinceShort(adminData.getShortName());
                        ipVo.setProvinceEngFull(adminData.getEngFullName());
                        ipVo.setProvinceEngShort(adminData.getEngShortName());
                        resetCommonField(ipVo, adminData);
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(city)) {
            adminDataSet = fullNameAdminDataMap.get(city);
            if (adminDataSet == null || adminDataSet.size() == 0) {
                adminDataSet = shortNameAdminDataMap.get(city);
            }
            if (adminDataSet != null && adminDataSet.size() > 0) {
                logger.debug("根据市或县级名称\t{},查询结果数为:\t{}", city, adminDataSet.size());
                String gb = "";
                if (StringUtils.isNotBlank(ipVo.getGb()) && adminDataSet.size() > 1) {
                    gb = StringUtils.substring(ipVo.getGb(), 0, Math.min(5, ipVo.getGb().length()));
                }
                for (AdminData adminData : adminDataSet) {
                    if (adminDataSet.size() > 1) {
                        if (StringUtils.startsWith(adminData.getGb(), gb)) {
                            ipVo.setCity(adminData.getFullName());
                            ipVo.setCityShort(adminData.getShortName());
                            ipVo.setCityEngFull(adminData.getEngFullName());
                            ipVo.setCityEngShort(adminData.getEngShortName());
                            resetCommonField(ipVo, adminData);
                            break;
                        }
                    } else {
                        ipVo.setCity(adminData.getFullName());
                        ipVo.setCityShort(adminData.getShortName());
                        ipVo.setCityEngFull(adminData.getEngFullName());
                        ipVo.setCityEngShort(adminData.getEngShortName());
                        resetCommonField(ipVo, adminData);
                    }
                }
            }

        }
        return ipVo;
    }

    private String convertStr(String data) {
        if (StringUtils.equals(data, "0")) {
            return null;
        }
        return data.trim();
    }


    private IpVo resetCommonField(IpVo ipVo, AdminData adminData) {
        if (StringUtils.isNotBlank(adminData.getGb())) {
            ipVo.setGb(adminData.getGb());
        }
        if (StringUtils.isNotBlank(adminData.getLng())) {
            ipVo.setLng(adminData.getLng());
        }
        if (StringUtils.isNotBlank(adminData.getLat())) {
            ipVo.setLat(adminData.getLat());
        }
        if (StringUtils.isNotBlank(adminData.getContinent())) {
            ipVo.setContinent(adminData.getContinent());
        }
        return ipVo;
    }


}
