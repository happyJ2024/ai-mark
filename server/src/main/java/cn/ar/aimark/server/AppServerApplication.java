package cn.ar.aimark.server;

import cn.ar.aimark.server.constant.ApplicationConst;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 应用入口
 *
 * @author yunjian.bian
 */
@SpringBootApplication
@MapperScan("cn.ar.aimark.server.mapper")
@ImportResource("classpath:aop-config.xml")
//@RefreshScope
@EnableTransactionManagement
public class AppServerApplication implements ApplicationRunner, ApplicationListener<ContextClosedEvent> {


    public static void main(String[] args) {

        ApplicationConst.init();
        SpringApplication.run(AppServerApplication.class, args);
    }


    @Value("${welcome.message}")
    private String welcomeMessage;

    /**
     * 在Spring Boot应用程序启动后执行代码的接口
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Hello from ASRaimarkServerApplication Runner");
        System.out.println(welcomeMessage);
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
