package com.cqupt.extractionrules.utils;

import com.cqupt.extractionrules.pojo.Template;
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

    private List<Template> templateList;

    private Template template;

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

    public R addTemplateList(List<Template> templateList){
        this.templateList = templateList;
        return this;
    }

    public R addTemplate(Template template){
        this.template = template;
        return this;
    }
}
