package cn.airesearch.aimarkserver.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 自定义系统配置
 * @author ZhangXi
 */
@Data
@Component
public class AppSetting {

    @Value("${app.resource.root}")
    private String resourceRoot;

    @Value("${app.resource.url.prefix}")
    private String urlPrefix;



}
