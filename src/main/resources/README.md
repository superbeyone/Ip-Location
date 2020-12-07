### 天地图IP定位服务

#### 配置文件说明

```yml
###################################### 自定义配置  #################################################
tdt:
  file-properties:
    region-csv: E:\data\ip\data\global_region.csv   # csv  文件位置 生成 ip2region.db文件使用
    ip-txt: E:\data\ip\data\ip.merge.txt      # txt 文件位置 同上
    region-db: E:\data\ip\data\ip2region.db   # db 文件位置 ip查询时使用
    admin-data-csv: E:\data\ip\data\AdminData.csv # admin data csv
  search-properties:
    type: B-tree      # 数据查询方式 可选项为: B-tree   Binary   Memory   默认 B-tree
```

- 说明：

    - region-csv
    - ip-txt
    - region-db
 
 上述三种文件可从 [GitHub](https://github.com/lionsoul2014/ip2region/tree/master/data)  或者 [Gitee](https://gitee.com/lionsoul/ip2region/tree/master/data) 下载
   
 admin-data-csv 文件 由 [`数据人员`] 提供
 
 项目开源地址：
 - https://github.com/lionsoul2014/ip2region
 - http://gitee.com/lionsoul/ip2region