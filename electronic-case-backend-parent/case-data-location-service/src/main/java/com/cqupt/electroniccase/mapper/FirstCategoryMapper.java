package com.cqupt.electroniccase.mapper;

import org.apache.ibatis.annotations.Mapper;
import pojo.FirstCategory;

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
}
