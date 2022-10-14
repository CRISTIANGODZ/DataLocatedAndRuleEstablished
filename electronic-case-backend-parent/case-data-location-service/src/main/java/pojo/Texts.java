package pojo;

import lombok.Data;

import java.util.Date;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 7:02
 * @Discriptioon
 */
@Data
public class Texts {
    private Long textId;
    private Long patientId;
    private Long firstCategoryId;
    private Long diseaseId;
    private String title;
    private String content;
    private Date updatetime;
    private Long themeId;
}
