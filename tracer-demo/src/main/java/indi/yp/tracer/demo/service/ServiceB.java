package indi.yp.tracer.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
@Slf4j
public class ServiceB {

    /**
     * 异步方法
     */
    @Async
    public Future<Void> asyncMethod() {
        log.info("this is an asyncMethod");
        return new AsyncResult<>(null);
    }
}
