package cn.ar.aimark.server.config;

import cn.ar.aimark.server.constant.ApplicationConst;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger文档配置
 * @author yunjian.bian
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration implements EnvironmentAware, WebMvcConfigurer {

    @Override
    public void setEnvironment(Environment environment) {

    }

    @Bean
    public Docket createRestApi() {
        List<Parameter> parameters = new ArrayList<>();
        //添加额外的参数到header中
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder.name(ApplicationConst.HEADER_Authorization_Key)
                .description("身份验证token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        parameters.add(parameterBuilder.build());


        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.ar"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(parameters)
                .apiInfo(apiInfo());
    }

    public SwaggerConfiguration() {
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger Doc")
                .contact(new Contact("yunjian.bian", "", "yunjian.bian@ai-research.cn"))
                .version("1.0")
                .build();
    }

}
