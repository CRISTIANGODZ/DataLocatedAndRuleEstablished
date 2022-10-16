package pojo;

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
    private Long diseaseId;
    private String title;
    private String content;
    private Date updatetime;
    private Long themeId;

    public Texts(Long patientId, Long firstCategoryId, Long diseaseId, String title, String content, Long themeId) {
        this.patientId = patientId;
        this.firstCategoryId = firstCategoryId;
        this.diseaseId = diseaseId;
        this.title = title;
        this.content = content;
        this.themeId = themeId;
    }


}
