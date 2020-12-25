package com.tdt.ip.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className IpUtil
 * @description Ip工具类
 * @date 2020-01-15 09:15
 **/

public class IpUtil {

    /**
     * 校验Ip是否合法
     *
     * @param ip ip
     * @return true 合法
     */
    public static boolean isIp(String ip) {
        String regex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
        if (Pattern.matches(regex, ip)) {
            return true;
        }
        return false;
    }

    public static String convertKey(String ip) {
        return "ip:" + ip;
    }

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
