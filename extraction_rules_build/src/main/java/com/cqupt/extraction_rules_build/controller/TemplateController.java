package com.cqupt.extraction_rules_build.controller;


import com.cqupt.extraction_rules_build.pojo.entity.Template;
import com.cqupt.extraction_rules_build.service.TemplateService;
import com.cqupt.extraction_rules_build.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auther DyingZhang
 * @Create 2022-10-18 下午 9:01
 * @Discriptioon
 */
@Api(tags = "模板操作")
@RestController
@RequestMapping("/template")
public class TemplateController {
    @Autowired
    TemplateService templateService;

    /**
     * 添加模板
     * @return
     */
    @ApiOperation(value = "添加模板", notes = "<font color='green'>描述：</font>添加模板")
    @PostMapping("/add")
    @ResponseBody
    public R addTemplateController(@RequestBody Template template){
        templateService.addTemplateService(template);
        return R.ok().code(302).message("添加成功！");
    }

    /**
     * 获取所有的标签模板
     */
    @ApiOperation(value = "获取模板", notes = "<font color='green'>描述：</font>获取所有的标签模板", response = Template.class)
    @GetMapping("/get/all/templates")
    @ResponseBody
    public R getAllTemplatesController(){
        List<Template> allTemplates = templateService.getAllTemplates();
        if (allTemplates.size() > 0){
            return R.ok().data("allTemplates", allTemplates).message("成功返回所有模板！");
        } else {
            return R.ok().message("目前并无模板存在！");
        }
    }

    /**
     * 根据id获取模板
     */
    @ApiOperation(value = "获取单个模板", notes = "<font color='green'>描述：</font>根据id获取模板", response = Template.class)
    @GetMapping("/get/single/instance")
    @ResponseBody
    public R getSingleTemplateController(@ApiParam(name = "templateId", required = true, value = "模板的id") Long templateId){
        Template template = templateService.getTemplateByTemplateId(templateId);
        if (template != null){
            return R.ok().data("template", template).message("获取模板成功！");
        } else {
            return R.error().message("没有该模板信息,获取失败！");
        }
    }

    /**
     * 根据id修改所有的标签模板
     * @return
     */
    @ApiOperation(value = "修改模板", notes = "<font color='green'>描述：</font>修改模板")
    @PutMapping("/update")
    @ResponseBody
    public R updateSingleTemplateController(@RequestBody Template template){
        Long templateId = template.getTemplateId();
        boolean isTemplateExists = templateService.isTemplateExists(templateId);
        if (isTemplateExists){
            templateService.deleteTemplateByTemplateId(templateId);
            templateService.addTemplateService(template);
            return R.ok().message("修改成功！");
        } else {
            return R.error().message("没有改模板信息，修改失败！");
        }
    }

    /**
     * 根据id删除标签模板
     */
    @ApiOperation(value = "删除模板", notes = "<font color='green'>描述：</font>根据id删除标签模板")
    @DeleteMapping("/delete")
    @ResponseBody
    public R deleteSingleTemplateController(@ApiParam(name = "templateId", required = true, value = "模板的id")Long templateId){
        boolean isTemplateExists = templateService.isTemplateExists(templateId);
        if (isTemplateExists){
            templateService.deleteTemplateByTemplateId(templateId);
            return R.ok().message("删除成功！");
        } else {
            return R.error().message("没有该模板信息，删除失败！");
        }
    }
}
