package pojo;

import lombok.Data;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 7:00
 * @Discriptioon
 */
@Data
public class FirstCategory {
    private Long firstCategoryId;
    private String firstCategoryName;

    public FirstCategory(String firstCategoryName) {
        this.firstCategoryName = firstCategoryName;
    }
}
