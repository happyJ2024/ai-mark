package cn.ar.aimark.server.config.context;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

/**
 * 全局上下文
 *
 * @author yunjian.bian
 */
public class RuntimeContext {
    /**
     * 公共线程池
     */
    private volatile static ExecutorService executorService;
    private volatile static ExecutorService scheduledExecutorService;

    /**
     * 初始化公共线程池
     */
    private static void initThreadPool() {
        if (executorService == null) {
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
            int corePoolSize = 2;
            int maximumPoolSize = 10;
            executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), namedThreadFactory);
        }
    }

    public static ExecutorService getExecutorService() {
        if (executorService == null) {
            synchronized (ExecutorService.class) {
                initThreadPool();
            }
        }
        return executorService;
    }

    public static ExecutorService getScheduledExecutorService(int corePoolSize) {
        if (scheduledExecutorService == null) {
            synchronized (ExecutorService.class) {
                initScheduledThreadPool(corePoolSize);
            }
        }
        return scheduledExecutorService;
    }

    private static void initScheduledThreadPool(int corePoolSize) {
        if (scheduledExecutorService == null) {

            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("scheduled-thread-call-runner-%d").setDaemon(true).build();

            scheduledExecutorService = new ScheduledThreadPoolExecutor(corePoolSize,
                    namedThreadFactory);
        }
    }
}
