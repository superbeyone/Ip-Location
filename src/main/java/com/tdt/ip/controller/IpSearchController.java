package com.tdt.ip.controller;

import com.tdt.ip.commons.DataInitBean;
import com.tdt.ip.commons.JsonResult;
import com.tdt.ip.commons.ResultEnum;
import com.tdt.ip.configuration.TdtIpConfig;
import com.tdt.ip.entity.IpVo;
import com.tdt.ip.service.IpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

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
    DataInitBean dataInitBean;

    @Autowired
    IpService ipService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/{ip}")
    public JsonResult<IpVo> getIpPosition(@PathVariable(name = "ip") String ip) throws Exception {


        String regionDb = tdtIpConfig.getFileProperties().getRegionDb();
        File regionDbFile = new File(regionDb);
        if (!regionDbFile.exists()) {
            logger.error("没有找到文件,{}", regionDb);
            return JsonResult.fail(ResultEnum.NO_FILE_FOUND);
        }
        IpVo ipVo = ipService.getIpPosition(ip, regionDbFile);

        return JsonResult.getInstance().success(ipVo);
    }


}
