package pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 8:32
 * @Discriptioon
 */
@Data
@NoArgsConstructor
public class Diseases {
    private Long diseaseId;
    private String diseaseName;

    public Diseases(String diseaseName) {
        this.diseaseName = diseaseName;
    }
}
