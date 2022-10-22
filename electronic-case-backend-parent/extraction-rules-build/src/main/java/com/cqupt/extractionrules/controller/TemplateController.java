package com.cqupt.extractionrules.controller;

import com.cqupt.extractionrules.pojo.Template;
import com.cqupt.extractionrules.service.TemplateService;
import com.cqupt.extractionrules.utils.R;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auther DyingZhang
 * @Create 2022-10-18 下午 9:01
 * @Discriptioon
 */
@RestController
@RequestMapping("/template")
public class TemplateController {
    @Autowired
    TemplateService templateService;

    /**
     * 添加模板
     * @return
     */
    @PostMapping("/add")
    public R addTemplateController(@RequestBody Template template){
        templateService.addTemplateService(template);
        return R.ok();
    }

    /**
     * 根据id获取模板
     */
    @GetMapping("/get/single/instance")
    public R getSingleTemplateController(){
        return R.ok();
    }

    /**
     * 获取所有的标签模板
     */
    @GetMapping("/get/all/templates")
    public R getAllTemplatesController(){
        List<Template> allTemplates = templateService.getAllTemplates();
        return R.ok().addTemplateList(allTemplates);
    }

    /**
     * 根据id修改所有的标签模板
     * @return
     */
    @PostMapping("/update")
    public R updateSingleTemplateController(){
        return R.ok();
    }

    /**
     * 根据id删除标签模板
     */
    @Delete("/delete")
    public R deleteSingleTemplateController(){
        return R.ok();
    }
}
