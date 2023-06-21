package com.cqupt.case_data_location.pojo.query;

import lombok.*;

/**
 * @auther DyingZhang
 * @Create 2023-06-21 下午 2:50
 * @Discriptioon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserQuery {
    private String name;
    private Integer offset;
    private Integer limits;
}
