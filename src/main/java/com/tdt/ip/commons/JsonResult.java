package com.tdt.ip.commons;

/**
 * @program: tool
 * @description:
 * @author: Mr.superbeyone
 * @create: 2018-10-18 16:37
 **/
public class JsonResult<T> {

    private Integer code;

    private String msg;

    private T data;


    public JsonResult() {
    }

    public static JsonResult getInstance() {
        return new JsonResult<>();
    }

    public JsonResult(int code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public JsonResult success(T data) {
        return new JsonResult(200, "成功", data);
    }

    public JsonResult success(ResultEnum resultCodeEnum, T data) {
        return new JsonResult(resultCodeEnum.getCode(), resultCodeEnum.getMsg(), data);
    }

    public static JsonResult success() {
        return new JsonResult(200, "成功", null);
    }

    public static JsonResult ok(String msg) {
        return new JsonResult(200, msg, null);
    }

    public static JsonResult success(ResultEnum resultCodeEnum) {
        return new JsonResult(resultCodeEnum.getCode(), resultCodeEnum.getMsg(), null);
    }

    public static JsonResult fail(int status, String message) {
        return new JsonResult(status, message, null);
    }

    public static JsonResult fail(ResultEnum resultCodeEnum) {
        return new JsonResult(resultCodeEnum.getCode(), resultCodeEnum.getMsg(), null);
    }

    public static JsonResult fail() {
        return new JsonResult(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMsg(), null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
