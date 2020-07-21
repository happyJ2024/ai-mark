package cn.airesearch.aimarkserver;

import cn.airesearch.aimarkserver.filter.RequestFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AimarkServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AimarkServerApplication.class, args);
    }


    @Bean
    public FilterRegistrationBean RequestFilterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new RequestFilter());
        registration.addUrlPatterns("/api/*");
        return registration;
    }

}
