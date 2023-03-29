package com.cqupt.electroniccase.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

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
