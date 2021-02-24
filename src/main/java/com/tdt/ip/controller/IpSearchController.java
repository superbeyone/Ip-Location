package com.tdt.ip.controller;

import com.alibaba.fastjson.JSON;
import com.tdt.ip.commons.JsonResult;
import com.tdt.ip.configuration.TdtIpConfig;
import com.tdt.ip.entity.IpVo;
import com.tdt.ip.service.IpService;
import com.tdt.ip.utils.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className IpSearchController
 * @description
 * @date 2020-01-02 16:45
 **/
@RestController
@RequestMapping("/ip")
public class IpSearchController {

    @Autowired
    TdtIpConfig tdtIpConfig;

    @Autowired
    IpService ipService;

    File regionDbFile = null;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final String localIpPrefix = "192.9.104.";

    @GetMapping("/{ip}")
    public JsonResult<IpVo> getIpPositionByIp(@PathVariable(name = "ip") String ip) throws Exception {

        if (regionDbFile == null) {
            File regionDbFile = initRegionDbFile();
            this.regionDbFile = regionDbFile;
        }
        IpVo ipVo = ipService.getIpPosition(ip, regionDbFile);

        return JsonResult.getInstance().success(ipVo);
    }

    private File initRegionDbFile() {
        String regionDb = tdtIpConfig.getFileProperties().getRegionDb();
        File regionDbFile = new File(regionDb);
        if (!regionDbFile.exists()) {
            logger.error("没有找到文件,{}", regionDb);
            throw new RuntimeException("没有找到数据库文件");
        }
        return regionDbFile;
    }

    @GetMapping
    public JsonResult<IpVo> getIpPosition(HttpServletRequest request) throws Exception {


        if (regionDbFile == null) {
            File regionDbFile = initRegionDbFile();
            this.regionDbFile = regionDbFile;
        }
        String ip = IpUtil.getIp(request);
        logger.debug("获取ip地址：{}", ip);
        if (StringUtils.startsWith(ip, localIpPrefix)) {
            ip = "218.244.250.66";
        }
        IpVo ipVo = ipService.getIpPosition(ip, regionDbFile);

        return JsonResult.getInstance().success(ipVo);
    }


    /**
     * 兼容老版本
     *
     * @param request req
     * @return map
     */
    @GetMapping("/getCityName")
    public String getCityName(HttpServletRequest request) throws Exception {
        String ip = IpUtil.getIp(request);
        logger.debug("获取ip地址：{}", ip);
        if (StringUtils.startsWith(ip, localIpPrefix)) {
            ip = "218.244.250.66";
        }
        String callback = request.getParameter("callback");
        IpVo ipVo = ipService.getIpPosition(ip, regionDbFile);
        Map<String, Object> map = new HashMap<>(8);
        if (ipVo != null) {
            map.put("cityName", ipVo.getCity());
            map.put("gbCode", ipVo.getGb());
            map.put("lon", ipVo.getLng());
            map.put("lat", ipVo.getLat());
            map.put("level", ipVo.getLevel());
        }
        String result = JSON.toJSONString(map);
        return callback + "(" + result + ")";
    }
}
