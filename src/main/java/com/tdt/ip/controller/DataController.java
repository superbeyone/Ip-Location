package com.tdt.ip.controller;

import com.tdt.ip.commons.DataInitBean;
import com.tdt.ip.commons.JsonResult;
import com.tdt.ip.configuration.TdtIpConfig;
import com.tdt.ip.utils.DbConfig;
import com.tdt.ip.utils.DbMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className DataController
 * @description
 * @date 2020-01-02 12:28
 **/
@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    TdtIpConfig tdtIpConfig;

    @Autowired
    DataInitBean dataInitBean;

    @PutMapping("/maker")
    public JsonResult<Void> initData() throws Exception {
        DbConfig config = new DbConfig();
        String txtPath = tdtIpConfig.getFileProperties().getIpTxt();
        String csvPath = tdtIpConfig.getFileProperties().getRegionCsv();
        String destDir = tdtIpConfig.getFileProperties().getRegionDb();
        DbMaker dbMaker = new DbMaker(config, txtPath, csvPath);
        File dbDestFile = new File(destDir);

        if (!dbDestFile.exists()) {
            dbDestFile.getParentFile().mkdirs();
        }
        dbMaker.make(destDir);
        return JsonResult.success();
    }

    @PutMapping("/adminCsv")
    public JsonResult<Void> initAdminData() {
        String adminDataCsv = tdtIpConfig.getFileProperties().getAdminDataCsv();
        File file = new File(adminDataCsv);
        if (!file.exists()) {
            return JsonResult.fail(404, "admin data csv 文件不存在");
        }
        dataInitBean.initAdminDataCsv();
        return JsonResult.success();
    }


}
