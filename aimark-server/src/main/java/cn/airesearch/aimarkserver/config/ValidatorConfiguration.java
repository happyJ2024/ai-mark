package cn.airesearch.aimarkserver.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 参数校验配置
 *
 * @author ZhangXi
 */
@Slf4j
@Configuration
public class ValidatorConfiguration implements WebMvcConfigurer {

    private final ResourceBundleMessageSource messageSource;

    @Autowired
    public ValidatorConfiguration(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        messageSource.addBasenames("i18n/validations/ValidatorMessages");
        bean.setValidationMessageSource(messageSource);
        bean.setProviderClass(HibernateValidator.class);
        return bean;
    }

}
