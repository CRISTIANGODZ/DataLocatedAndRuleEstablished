package com.cqupt.extractionrules.mapper;

import com.cqupt.extractionrules.pojo.TemplateRules;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @auther DyingZhang
 * @Create 2022-10-18 下午 9:07
 * @Discriptioon
 */
@Mapper
public interface TemplateRulesMapper {

    /**
     * 添加templateRules
     * @param templateRules
     */
    void addTemplateRules(TemplateRules templateRules);

    /**
     * 根据templateId获取TemplateRules对象
     * @param templateId
     * @return
     */
    TemplateRules getTemplateRulesByTemplateId(@Param("templateId") Long templateId);
}
