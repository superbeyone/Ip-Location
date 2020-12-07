package com.tdt.ip.commons;

/**
 * @author superbeyone
 */

public enum ResultEnum {

    /**
     * 统一返回结果状态码
     */
    FAIL(400, "失败"),

    AUTH_FAIL(403, "用户名或密码错误"),

    NO_AUTHORIZE(401, "无操作权限"),

    NO_FILE_FOUND(10001, "没有找到数据文件"),
    
    IP_NOT_LEGITIMATE(1003, "参数不合法"),

    LACK_IMPORT_PARAM(50000, "缺少重要参数");


    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }


}
