package com.guitu18.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * JsonResult
 *
 * @author zhangkuan
 * @date 2019/8/19
 */
public class JsonResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public JsonResult() {
        put("code", 0);
        put("success", true);
        put("msg", "操作成功");
        put("data", "");
    }

    public static JsonResult ok() {
        return new JsonResult();
    }

    public static JsonResult ok(String msg) {
        JsonResult result = new JsonResult();
        result.put("msg", msg);
        return result;
    }

    public static JsonResult ok(Map<String, Object> map) {
        JsonResult result = new JsonResult();
        result.putAll(map);
        return result;
    }

    public static JsonResult ok(Object object) {
        JsonResult result = new JsonResult();
        result.put("data", object);
        return result;
    }


    public static JsonResult ok(String msg, Object object) {
        JsonResult result = new JsonResult();
        result.put("msg", msg);
        result.put("data", object);
        return result;
    }

    public static JsonResult error() {
        return error(500, "系统内部出错，请联系管理员");
    }

    public static JsonResult error(String msg) {
        return error(500, msg);
    }

    public static JsonResult error(int code, String msg) {
        JsonResult result = new JsonResult();
        result.put("code", code);
        result.put("success", false);
        result.put("msg", msg);
        return result;
    }

    @Override
    public JsonResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}