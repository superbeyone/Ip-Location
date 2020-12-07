package com.tdt.ip;

import com.tdt.ip.utils.IpUtil;
import org.apache.tomcat.util.net.IPv6Utils;
import org.junit.jupiter.api.Test;
import sun.net.util.IPAddressUtil;

//@SpringBootTest
class IpServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void ip() {
        String ip = "192.9.10.255";
        long start = System.nanoTime();
        boolean result = IpUtil.isIp(ip);
        System.out.println(System.nanoTime() - start);
        start = System.nanoTime();
        boolean iPv4LiteralAddress = IPAddressUtil.isIPv4LiteralAddress(ip);
        System.out.println(System.nanoTime() - start);
        System.out.println(result + "\t" + iPv4LiteralAddress);

        System.out.println(IPv6Utils.canonize(ip));

        System.out.println(IpUtil.convertKey(ip));
    }


}
