package com.cqupt.case_data_location.mapper;

import com.cqupt.case_data_location.pojo.entity.Themes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 根据themeId删除相关信息
     * @param themeId
     */
    void deleteThemes(@Param("themeId") Long themeId);
}
