package com.cqupt.extraction_rules_build.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

//统一返回结果的类
@Data
public class R {

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<String, Object>();

    //把构造方法私有
    private R() {}

    //成功静态方法
    public static R ok() {
        R r = new R();
        r.setCode(200);
        r.setMessage("成功");
        return r;
    }

    //失败静态方法
    public static R error() {
        R r = new R();
        r.setCode(400);
        r.setMessage("失败");
        return r;
    }

    /**
     * 设置状态码
     * @param code
     * @return
     */
    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    /**
     * 设置信息
     * @param message
     * @return
     */
    public R message(String message){
        this.setMessage(message);
        return this;
    }

    /**
     * 以key，value对的形式添加数据
     * @param key
     * @param value
     * @return
     */
    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    /**
     * 直接添加map
     * @param map
     * @return
     */
    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

}
