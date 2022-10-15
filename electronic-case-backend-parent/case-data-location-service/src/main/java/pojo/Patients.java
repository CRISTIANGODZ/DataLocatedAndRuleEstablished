package pojo;

import lombok.Data;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 6:55
 * @Discriptioon
 */
@Data
public class Patients {
    private Long patientId;
    private String name;
    private String diseaseIdList;
    private Long firstCategory;

    public Patients(String name) {
        this.name = name;
    }
}
