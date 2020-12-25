package com.tdt.ip.commons;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.HashMultimap;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.tdt.ip.configuration.TdtIpConfig;
import com.tdt.ip.entity.AdminData;
import com.tdt.ip.utils.DbConfig;
import com.tdt.ip.utils.DbSearcher;
import com.tdt.ip.utils.coding.EncodingDetect;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Mr.superbeyone
 * @project ip-service
 * @className DataInitBean
 * @description
 * @date 2020-01-02 15:47
 **/
@Component
public class DataInitBean implements ApplicationRunner {

    @Autowired
    TdtIpConfig tdtIpConfig;

    @Autowired
    RestTemplate restTemplate;

    private DbSearcher dbSearcher;

    private Method method;

    private HashMultimap<String, AdminData> fullNameAdminDataMap = HashMultimap.create();
    private HashMultimap<String, AdminData> shortNameAdminDataMap = HashMultimap.create();


    public DbSearcher getDbSearcher() {
        return dbSearcher;
    }

    public Method getMethod() {
        return method;
    }

    public HashMultimap<String, AdminData> getFullNameAdminDataMap() {
        return fullNameAdminDataMap;
    }

    public HashMultimap<String, AdminData> getShortNameAdminDataMap() {
        return shortNameAdminDataMap;
    }

    private Map<String, Integer> globalGbAndLevelMap = new HashMap<>(4096);

    public Map<String, Integer> getGlobalGbAndLevelMap() {
        return globalGbAndLevelMap;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        initCityLevel();

        initAdminDataCsv();

        initDataSearcher();

        initMethod();
    }


    /**
     * 初始化行政区划信息
     */
    private void initCityLevel() {

        try {
            String administrativeService = tdtIpConfig.getServiceUrl().getAdministrativeService();
            String response = restTemplate.getForObject(administrativeService + "/region/county", String.class);
            if (StringUtils.isNotBlank(response)) {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject countryObj = jsonObject.getJSONObject("data");
                globalGbAndLevelMap.put("000000", 4);
                convertJsonObj(countryObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertJsonObj(JSONObject countryObj) {
        JSONArray childList = countryObj.getJSONArray("child");
        if (childList != null && childList.size() > 0) {
            for (Object child : childList) {
                if (child == null) {
                    continue;
                }
                JSONObject jsonObject = JSON.parseObject(String.valueOf(child));
                if (jsonObject == null) {
                    continue;
                }
                String gbCode = jsonObject.getString("gbCode");
                int level = jsonObject.getIntValue("level");
                String key = StringUtils.substringAfter(gbCode, "156");
                globalGbAndLevelMap.put(key, level);
                convertJsonObj(jsonObject);
            }
        }
    }

    private void initDataSearcher() throws Exception {

        DbConfig config = new DbConfig();
        DbSearcher searcher = new DbSearcher(config, tdtIpConfig.getFileProperties().getRegionDb());
        this.dbSearcher = searcher;
    }

    private void initMethod() throws Exception {

        String searchType = tdtIpConfig.getSearchProperties().getType();
        changeSearchType(searchType);

    }

    public void changeSearchType(String searchType) throws Exception {
        int algorithm = DbSearcher.BTREE_ALGORITHM;
        if (searchType.equalsIgnoreCase("binary")) {
            algorithm = DbSearcher.BINARY_ALGORITHM;
        } else if (searchType.equalsIgnoreCase("memory")) {
            algorithm = DbSearcher.MEMORY_ALGORITYM;
        }

        DbSearcher searcher = getDbSearcher();
        Method method;
        switch (algorithm) {
            case DbSearcher.BTREE_ALGORITHM:
                method = searcher.getClass().getMethod("btreeSearch", String.class);
                break;
            case DbSearcher.BINARY_ALGORITHM:
                method = searcher.getClass().getMethod("binarySearch", String.class);
                break;
            case DbSearcher.MEMORY_ALGORITYM:
                method = searcher.getClass().getMethod("memorySearch", String.class);
                break;
            default:
                method = searcher.getClass().getMethod("btreeSearch", String.class);
        }
        this.method = method;
    }


    public void initAdminDataCsv() {
        String adminDataCsv = tdtIpConfig.getFileProperties().getAdminDataCsv();

        String encode = EncodingDetect.getFileEncode(adminDataCsv);
        try (CSVReader csvReader = new CSVReaderBuilder(new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(adminDataCsv)), encode))).build()) {
            Iterator<String[]> iterator = csvReader.iterator();
            //清空数据
            fullNameAdminDataMap.clear();
            shortNameAdminDataMap.clear();

            while (iterator.hasNext()) {
                String[] data = iterator.next();
                if (data == null || data.length < 11) {
                    continue;
                }
                String fullName = convertData(data[0]);
                String shortName = convertData(data[1]);
//                String engFullName = data[2];
//                String engShortName = data[3];
                String gbCode = convertData(data[4]);
                String gb = convertData(data[5]) == null ? gbCode : convertData(data[5]);
//                String gb = data[5];
//                String lng = data[6];
//                String lat = data[7];
//                String continent = data[9];
                AdminData adminData = AdminData.builder().fullName(fullName).shortName(shortName)
                        .engFullName(convertData(data[2])).engShortName(convertData(data[3]))
                        .gbCode(gbCode).gb(gb)
                        .lng(convertData(data[6])).lat(convertData(data[7])).continent(convertData(data[9])).build();
                fullNameAdminDataMap.put(fullName, adminData);
                shortNameAdminDataMap.put(shortName, adminData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertData(String data) {
        if (StringUtils.isNotBlank(data) && !StringUtils.equals(data, "0")) {
            return data.trim();
        }
        return null;
    }


}
