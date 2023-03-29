package com.cqupt.electroniccase.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 7:00
 * @Discriptioon
 */
@Data
@NoArgsConstructor
public class FirstCategory {
    private Long firstCategoryId;
    private String firstCategoryName;

    public FirstCategory(String firstCategoryName) {
        this.firstCategoryName = firstCategoryName;
    }
}
