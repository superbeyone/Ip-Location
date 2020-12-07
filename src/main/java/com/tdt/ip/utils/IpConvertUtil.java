package com.tdt.ip.utils;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className IpConvertUtil
 * @description
 * @date 2020-01-02 14:51
 **/

public class IpConvertUtil {

    public static long convertToLong(String ip) {
        String[] p = ip.split("\\.");
        if (p.length != 4) {
            return 0;
        }

        int p1 = ((Integer.valueOf(p[0]) << 24) & 0xFF000000);
        int p2 = ((Integer.valueOf(p[1]) << 16) & 0x00FF0000);
        int p3 = ((Integer.valueOf(p[2]) << 8) & 0x0000FF00);
        int p4 = ((Integer.valueOf(p[3]) << 0) & 0x000000FF);

        return ((p1 | p2 | p3 | p4) & 0xFFFFFFFFL);
    }


}
