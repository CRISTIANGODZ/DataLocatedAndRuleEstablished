package com.cqupt.case_data_location.mapper;

import com.cqupt.case_data_location.pojo.entity.FirstCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 8:52
 * @Discriptioon
 */
@Mapper
public interface FirstCategoryMapper {

    /**
     * 添加第一种类信息，并返回自增主键ID
     * @param firstCategory
     */
    void addFirstCategoryMapper(FirstCategory firstCategory);

    /**
     * 根据firstCategoryId查询firstCategoryName
     * @param firstCategoryId
     * @return
     */
    FirstCategory getFirstCategory(@Param("firstCategoryId") Long firstCategoryId);

    /**
     * 删除第一种类的信息
     * @param firstCategoryId
     */
    void deleteFirstCategoryById(@Param("firstCategoryId") Long firstCategoryId);
}
