package com.cqupt.extractionrules.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther DyingZhang
 * @Create 2022-10-18 下午 8:48
 * @Discriptioon
 */
@Data
@NoArgsConstructor
public class EntityLabelCategory {
    private Long labelCategoryId;

    private Long templateId;

    private String title;

    private String color;

    private String borderColor;

    private LabelRules labelRules;

}
