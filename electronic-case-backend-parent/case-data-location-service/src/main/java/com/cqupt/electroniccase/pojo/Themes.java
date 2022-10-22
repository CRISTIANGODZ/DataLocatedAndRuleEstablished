package com.cqupt.electroniccase.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 7:01
 * @Discriptioon
 */
@Data
@NoArgsConstructor
public class Themes {
    private Long themeId;
    private String themeName;

    public Themes(String themeName) {
        this.themeName = themeName;
    }
}
