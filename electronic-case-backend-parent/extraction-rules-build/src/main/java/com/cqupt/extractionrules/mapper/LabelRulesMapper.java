package com.cqupt.extractionrules.mapper;

import com.cqupt.extractionrules.pojo.LabelRules;
import org.apache.ibatis.annotations.Mapper;

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
}
