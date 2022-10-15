package com.cqupt.electroniccase.mapper;

import org.apache.ibatis.annotations.Mapper;
import pojo.Themes;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 8:53
 * @Discriptioon
 */
@Mapper
public interface ThemesMapper {

    /**
     * 添加theme的key进入theme数据库，并且返回主键id
     * @param themes
     */
    void addTheme(Themes themes);
}
