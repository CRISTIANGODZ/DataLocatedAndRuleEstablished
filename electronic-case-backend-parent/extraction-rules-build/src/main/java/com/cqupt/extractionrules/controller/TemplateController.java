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
        return R.ok().message("添加成功！");
    }

    /**
     * 根据id获取模板
     */
    @GetMapping("/get/single/instance")
    public R getSingleTemplateController(Long templateId){
        Template template = templateService.getTemplateByTemplateId(templateId);
        return R.ok().addTemplate(template).message("获取模板成功！");
    }

    /**
     * 获取所有的标签模板
     */
    @GetMapping("/get/all/templates")
    public R getAllTemplatesController(){
        List<Template> allTemplates = templateService.getAllTemplates();
        return R.ok().addTemplateList(allTemplates).message("成功返回所有模板！");
    }

    /**
     * 根据id修改所有的标签模板
     * @return
     */
    @PutMapping("/update")
    public R updateSingleTemplateController(){
        return R.ok().message("修改成功！");
    }

    /**
     * 根据id删除标签模板
     */
    @DeleteMapping("/delete")
    public R deleteSingleTemplateController(Long templateId){
        templateService.deleteTemplateByTemplateId(templateId);
        return R.ok().message("删除成功！");
    }
}
