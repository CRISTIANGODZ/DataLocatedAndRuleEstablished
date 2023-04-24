package com.cqupt.case_data_location;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @auther DyingZhang
 * @Create 2023-04-22 下午 6:59
 * @Discriptioon
 */
@SpringBootApplication
@EnableSwagger2
public class CaseDataLocation {
    public static void main(String[] args) {
        SpringApplication.run(CaseDataLocation.class, args);
    }
}
