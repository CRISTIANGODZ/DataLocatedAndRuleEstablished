package com.cqupt.extraction_rules_build.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther DyingZhang
 * @Create 2022-10-18 下午 8:46
 * @Discriptioon
 */
@Data
@NoArgsConstructor
public class TemplateRules {
    private Long templateRuleId;

    private Long templateId;

    private String templateDesc;
}
