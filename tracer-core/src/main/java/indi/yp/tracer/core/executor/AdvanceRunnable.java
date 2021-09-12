package indi.yp.tracer.core.executor;


import indi.yp.tracer.core.constant.CommonConstants;
import indi.yp.tracer.core.context.MethodInfo;
import indi.yp.tracer.core.context.TraceThreadContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdvanceRunnable implements Runnable {
    private final Runnable runnable;
    private final MethodInfo parentMethodInfo;

    public AdvanceRunnable(Runnable runnable) {
        this.runnable = runnable;
        parentMethodInfo = TraceThreadContext.getContext().getMethodInfoStack().peek();
    }

    @Override
    public void run() {
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
        runnable.run();
        methodInfo.setTimeInMs(System.currentTimeMillis() - methodInfo.getStartTime());
        TraceThreadContext.getContext().remove();
    }
}