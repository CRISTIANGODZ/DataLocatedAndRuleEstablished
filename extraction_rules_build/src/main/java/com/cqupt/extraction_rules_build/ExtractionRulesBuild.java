package com.cqupt.extraction_rules_build;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @auther DyingZhang
 * @Create 2023-04-23 上午 8:45
 * @Discriptioon
 */
@EnableSwagger2
@SpringBootApplication
public class ExtractionRulesBuild {
    public static void main(String[] args) {
        SpringApplication.run(ExtractionRulesBuild.class, args);
    }
}
