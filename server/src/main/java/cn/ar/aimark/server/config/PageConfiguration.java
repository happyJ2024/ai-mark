package cn.ar.aimark.server.config;


import cn.asr.appframework.utility.springext.YamlPropertySourceFactory;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

/**
 * 数据库分页配置
 * @author yunjian.bian
 */
@Configuration
@PropertySource(value = "classpath:page-config.yml", factory = YamlPropertySourceFactory.class)
public class PageConfiguration {

    @Value("${pagehelper.helperDialect}")
    private String helperDialect;

    @Value("${pagehelper.offsetAsPageNum}")
    private String offsetAsPageNum;

    @Value("${pagehelper.rowBoundsWithCount}")
    private String rowBoundsWithCount;

    @Value("${pagehelper.reasonable}")
    private String reasonable;

    @Bean
    @Primary
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        //数据库
        properties.setProperty("helperDialect", helperDialect);
        //是否将参数offset作为PageNum使用
        properties.setProperty("offsetAsPageNum", offsetAsPageNum);
        //是否进行count查询
        properties.setProperty("rowBoundsWithCount", rowBoundsWithCount);
        //是否分页合理化
        properties.setProperty("reasonable", reasonable);
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
