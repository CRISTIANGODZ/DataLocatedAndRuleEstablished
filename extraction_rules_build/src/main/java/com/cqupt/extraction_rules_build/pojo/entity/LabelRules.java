package com.cqupt.extraction_rules_build.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther DyingZhang
 * @Create 2022-10-18 下午 8:50
 * @Discriptioon
 */
@Data
@NoArgsConstructor
public class LabelRules {
    private Long labelRuleId;

    private Long labelCategoryId;

    private String labelDescription;
}