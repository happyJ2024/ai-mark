package cn.airesearch.aimarkserver.config;

import cn.airesearch.aimarkserver.constant.AppConst;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * 异步任务配置
 *
 * @author ZhangXi
 */
@Slf4j
@EnableAsync
@Configuration
public class AsyncConfiguration {

    //fixme 可配置自定义线程池，在@Async中使用

    @Bean(name = AppConst.EXECUTOR_CONVERT)
    public Executor convertExecutor() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("convert-")
                .setDaemon(true)
                .build();
        int threads = Runtime.getRuntime().availableProcessors() + 1;
        return new ThreadPoolExecutor(threads, 2*threads, 5, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(1024), threadFactory,
                (r, executor) -> {
                    log.info("convert task is running !");
                });
    }




}
