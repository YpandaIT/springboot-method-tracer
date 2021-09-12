package indi.yp.tracer.core.executor;

import indi.yp.tracer.core.constant.CommonConstants;
import indi.yp.tracer.core.context.MethodInfo;
import indi.yp.tracer.core.context.TraceThreadContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
@Getter
public class AdvanceCallable<V> implements Callable<V> {
    private final Callable<V> callable;
    private final MethodInfo parentMethodInfo;

    public AdvanceCallable(Callable<V> callable) {
        this.callable = callable;
        parentMethodInfo = TraceThreadContext.getContext().getMethodInfoStack().peek();
    }

    @Override
    public V call() throws Exception {
        TraceThreadContext context = TraceThreadContext.getContext();
        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setMethodName(CommonConstants.ASYNC_METHOD_ROOT);
        methodInfo.setCurrentThreadName(Thread.currentThread().getName());
        methodInfo.setStartTime(System.currentTimeMillis());
        try {
            if (null != parentMethodInfo) {
                parentMethodInfo.setAsyncContext(context.getMethodInfoStack());
            }
            context.getMethodInfoStack().push(methodInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        V v = callable.call();
        methodInfo.setTimeInMs(System.currentTimeMillis() - methodInfo.getStartTime());
        TraceThreadContext.getContext().remove();
        return v;
    }
}