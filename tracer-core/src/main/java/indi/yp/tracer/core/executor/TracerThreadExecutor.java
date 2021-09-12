package indi.yp.tracer.core.executor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class TracerThreadExecutor extends ThreadPoolTaskExecutor {

    @Override
    public void execute(Runnable task) {
        AdvanceRunnable runnable = new AdvanceRunnable(task);
        super.execute(runnable);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        AdvanceCallable<T> callable = new AdvanceCallable<>(task);
        return super.submit(callable);
    }
}
