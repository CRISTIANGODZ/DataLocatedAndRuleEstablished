package com.cqupt.case_data_location.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @auther DyingZhang
 * @Create 2023-04-24 下午 2:33
 * @Discriptioon
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        //ApiInfo 就是API帮助文档的描述信息。 information
        ApiInfo apiInfo =
                new ApiInfoBuilder()
                        .contact(   //配置swagger文档主题内容。
                                new Contact(
                                        "张丁野", //是文档的发布者名称
                                        "http://localhost:8081/", //是文档发布者的网站地址。企业网站
                                        "1535661945@qq.com")  //文档发布者的电子邮箱
                        )
                        .title("临床病历智能提取和标注\n")
                        .description("临床病历智能提取和标注项目\n")
                        .version("2.0")
                        .build();
        //给docket上下文配置api描述信息
        docket.apiInfo(apiInfo);

        return docket;
    }
}
