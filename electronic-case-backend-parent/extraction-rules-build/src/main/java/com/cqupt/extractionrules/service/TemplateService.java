package com.cqupt.extractionrules.service;

import com.cqupt.extractionrules.pojo.Template;

import java.util.List;

/**
 * @auther DyingZhang
 * @Create 2022-10-18 下午 9:04
 * @Discriptioon
 */
public interface TemplateService {

    /**
     * 添加模板
     * @param template
     * @return
     */
    Long addTemplateService(Template template);

    /**
     * 获取所有的标签模板
     */
    List<Template> getAllTemplates();

    /**
     * 根据templateId获取模板
     * @param templateId
     * @return
     */
    Template getTemplateByTemplateId(Long templateId);

    /**
     * 根据templateId删除模板
     * @param templateId
     */
    void deleteTemplateByTemplateId(Long templateId);

    /**
     * 根据templateId判断template是否存在
     * @param templateId
     * @return
     */
    boolean isTemplateExists(Long templateId);
}
