package pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 6:55
 * @Discriptioon
 */
@Data
@NoArgsConstructor
public class Patients {
    private Long patientId;
    private String name;
    private String diseaseIdList;
    private Long firstCategory;

    public Patients(String name) {
        this.name = name;
    }
}
