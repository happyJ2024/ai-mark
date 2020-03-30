package cn.ar.aimark.server.config;

import cn.asr.appframework.utility.springext.StringToDateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;

/**
 * SpringMVC 配置
 * @author yunjian.bian
 */
@Configuration
public class SpringMVCConfiguration {

    private final RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    public SpringMVCConfiguration(RequestMappingHandlerAdapter handlerAdapter) {
        this.handlerAdapter = handlerAdapter;
    }

    /**
     * SpringMVC自动进行类型转换
     * 向GenericConversionService中增加字符串转Date的功能
     */
    @PostConstruct
    public void initEditableValidation() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();
        if (null != initializer && null != initializer.getConversionService()) {
            GenericConversionService service = (GenericConversionService) initializer.getConversionService();
            service.addConverter(new StringToDateConverter());
        }
    }

}
