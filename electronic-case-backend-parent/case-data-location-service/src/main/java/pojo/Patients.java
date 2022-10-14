package pojo;

import lombok.Data;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 6:55
 * @Discriptioon
 */
@Data
public class Patients {
    private Long patient_id;
    private String name;
    private String diseaseIdList;
    private Long firstCategoryId;

    public Patients(String name) {
        this.name = name;
    }
}
