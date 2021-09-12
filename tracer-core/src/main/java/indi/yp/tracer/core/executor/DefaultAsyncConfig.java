package indi.yp.tracer.core.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;

/**
 * 默认线程池改造
 */
@Service
@Slf4j
@Configuration
public class DefaultAsyncConfig implements AsyncConfigurer {

    @PostConstruct
    private void exec() {
        log.info("init");
    }


    @Override
    public Executor getAsyncExecutor() {
        TracerThreadExecutor threadPool = new TracerThreadExecutor();
        threadPool.setCorePoolSize(20);
        threadPool.setMaxPoolSize(20);
        threadPool.setAwaitTerminationSeconds(60 * 15);
        threadPool.setThreadNamePrefix("default-task-");
        threadPool.initialize();
        return threadPool;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {

        return (throwable, method, objects) ->
                log.error("async method exception ,method name :{}, error:{}",
                        method.getName(), throwable.getMessage(), throwable);
    }

}
