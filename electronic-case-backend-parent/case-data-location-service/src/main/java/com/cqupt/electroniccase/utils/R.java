package com.cqupt.electroniccase.utils;

import com.cqupt.electroniccase.domain.entity.Texts;
import lombok.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//统一返回结果的类
@Data
public class R {

    private Boolean success;

    private Integer code;

    private String message;

    private Texts texts;

    private List patientThemeTextList;

    private Map<String, Object> data = new HashMap<String, Object>();

    //把构造方法私有
    private R() {}

    //成功静态方法
    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage("成功");
        return r;
    }

    //失败静态方法
    public static R error() {
        R r = new R();
        r.setSuccess(false);
        r.setCode(400);
        r.setMessage("失败");
        return r;
    }

    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

    public R addList(List list){
        this.patientThemeTextList = list;
        return this;
    }

    public R addTexts(Texts texts){
        this.texts = texts;
        return this;
    }
}
