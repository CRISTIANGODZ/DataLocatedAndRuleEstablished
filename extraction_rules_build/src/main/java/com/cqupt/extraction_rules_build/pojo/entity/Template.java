package com.cqupt.extraction_rules_build.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @auther DyingZhang
 * @Create 2022-10-18 下午 8:45
 * @Discriptioon
 */
@Data
@NoArgsConstructor
public class Template {
    private Long templateId;

    private String templateName;

    private List<EntityLabelCategory> entityLabelCategoryList;

    private TemplateRules templateRules;
}
