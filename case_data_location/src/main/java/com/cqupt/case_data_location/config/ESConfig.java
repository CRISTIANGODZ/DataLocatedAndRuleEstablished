package com.cqupt.case_data_location.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @auther DyingZhang
 * @Create 2023-04-22 下午 9:02
 * @Discriptioon
 */
@Configuration
public class ESConfig {

    @Autowired
    AddrESConfig addrESConfig;

    @Bean("esClient")
    public RestHighLevelClient restHighLevelClient() {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(addrESConfig.getIp(), addrESConfig.getPort())));
        return client;
    }

}
