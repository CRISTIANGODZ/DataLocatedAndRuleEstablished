package com.cqupt.extraction_rules_build.mapper;

import com.cqupt.extraction_rules_build.pojo.entity.EntityLabelCategory;
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

    /**
     * 根据templateId删除EntityLabelCategory
     * @param templateId
     */
    void deleteEntityLabelCategoryByTemplateId(@Param("templateId") Long templateId);
}