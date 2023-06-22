package com.cqupt.case_data_location.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 7:02
 * @Discriptioon
 */
@Data
@NoArgsConstructor
public class Texts {
    private Long textId;
    private Long patientId;
    private Long firstCategoryId;
    private String title;
    private String content;
    private Date updatetime;
    private Long themeId;

    public Texts(Long patientId, Long firstCategoryId, String title, String content, Long themeId) {
        this.patientId = patientId;
        this.firstCategoryId = firstCategoryId;
        this.title = title;
        this.content = content;
        this.themeId = themeId;
    }


    public Texts(Long textId, Long themeId) {
        this.textId = textId;
        this.themeId = themeId;
    }
}
