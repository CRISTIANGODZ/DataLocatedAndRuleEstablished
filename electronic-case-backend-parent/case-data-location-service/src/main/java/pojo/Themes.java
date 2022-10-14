package pojo;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

/**
 * @auther DyingZhang
 * @Create 2022-10-14 下午 7:01
 * @Discriptioon
 */
@Data
public class Themes {
    private Long themeId;
    private String themeName;

}
