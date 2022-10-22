package com.cqupt.extractionrules.mapper;

import com.cqupt.extractionrules.pojo.EntityLabelCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther DyingZhang
 * @Create 2022-10-18 下午 9:06
 * @Discriptioon
 */
@Mapper
public interface EntityLabelCategoryMapper {

    /**
     * 添加一条entityLabelCategory记录
     * @param entityLabelCategory
     */
    void addEntityLabelCategory(EntityLabelCategory entityLabelCategory);

    /**
     * 根据templateId获取EntityLabelCategory
     * @param templateId
     * @return
     */
    List<EntityLabelCategory> getEntityLabelCategoryByTemplateId(@Param("templateId") Long templateId);
}
