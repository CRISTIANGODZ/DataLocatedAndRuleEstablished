package com.cqupt.case_data_location.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @auther DyingZhang
 * @Create 2023-04-23 下午 2:39
 * @Discriptioon
 */
@Data
@Component
@ConfigurationProperties(prefix = "addr-es")
public class AddrESConfig {
    private String ip;
    private Integer port;
}
