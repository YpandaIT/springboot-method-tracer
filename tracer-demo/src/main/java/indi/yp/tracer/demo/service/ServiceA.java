package indi.yp.tracer.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
@Slf4j
public class ServiceA {
    @Autowired
    private ServiceB serviceB;

    /**
     * 同步方法
     */
    public void syncMethod() {
        log.info("this is a syncMethod");
        Future<Void> future = serviceB.asyncMethod();
        try {
            future.get();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
