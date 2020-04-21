package cn.airesearch.aimarkserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * {@link ApplicationContext} 对象获取类
 * 为普通对象获取spring bean提供入口
 * @author ZhangXi
 */
@Slf4j
@Component
@Lazy(false)
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext thisApplicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.thisApplicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return thisApplicationContext;
    }

    public static Object getBean(String name) {
        return thisApplicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> tClass) {
        return thisApplicationContext.getBean(tClass);
    }

}
