package cn.airesearch.aimarkserver.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhangXi
 */
@OpenAPIDefinition(
        info = @Info(
                title = "AI-mark Server API",
                version = "v1.0.1",
                description = "图片标记服务"
        )
)
@Configuration
public class ApiDocConfiguration {
}
