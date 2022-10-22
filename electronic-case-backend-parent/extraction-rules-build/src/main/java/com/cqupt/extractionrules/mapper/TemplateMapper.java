package com.cqupt.extractionrules.mapper;

import com.cqupt.extractionrules.pojo.Template;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther DyingZhang
 * @Create 2022-10-18 下午 9:07
 * @Discriptioon
 */
@Mapper
public interface TemplateMapper {

    /**
     * 添加template
     * @param template
     */
    void addTemplate(Template template);

    /**
     * 获取所有template模板
     * @return
     */
    List<Template> getAllTemplates();

    /**
     * 根据templateId获取template
     * @param templateId
     * @return
     */
    Template getTemplateByTemplateId(@Param("templateId") Long templateId);
}
