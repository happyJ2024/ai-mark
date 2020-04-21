package cn.airesearch.aimarkserver.config;

import cn.airesearch.aimarkserver.bean.AppSetting;
import cn.airesearch.aimarkserver.constant.AppConst;
import cn.airesearch.aimarkserver.constant.ResourceConst;
import cn.airesearch.aimarkserver.tool.IoTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 简易文件服务器配置
 *
 * @author ZhangXi
 */
@Profile(AppConst.PROFILE_DEV)
@Configuration
public class SimpleFileServerConfiguration implements WebMvcConfigurer {

    private final AppSetting appSetting;

    @Autowired
    public SimpleFileServerConfiguration(AppSetting appSetting) {
        this.appSetting = appSetting;
    }

    /**
     * springboot可以将本地文件地址映射为虚拟网络地址，从而实现简单的文件服务器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourcesPath = appSetting.getResourceRoot() + IoTool.FILE_PATH_SEPARATOR + ResourceConst.RESOURCES;
        registry.addResourceHandler("/resources/**").addResourceLocations("file:"+resourcesPath);
    }
}
