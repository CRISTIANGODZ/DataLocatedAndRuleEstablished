package com.cqupt.extractionrules.mapper;

import com.cqupt.extractionrules.pojo.LabelRules;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @auther DyingZhang
 * @Create 2022-10-18 下午 9:06
 * @Discriptioon
 */
@Mapper
public interface LabelRulesMapper {

    /**
     * 添加一条LabelRules
     * @param labelRules
     */
    void addLabelRules(LabelRules labelRules);

    /**
     * 根据labelCategoryId获取LabelRules
     * @param labelCategoryId
     * @return
     */
    LabelRules getLabelRules(@Param("labelCategoryId") Long labelCategoryId);

    /**
     * 根据labelCategoryId删除labelRules
     * @param labelCategoryId
     */
    void deleteLabelRulesByLabelCategoryId(@Param("labelCategoryId") Long labelCategoryId);
}
