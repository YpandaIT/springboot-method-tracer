package indi.yp.tracer.core.context;

import lombok.Getter;
import lombok.Setter;

import java.util.Deque;
import java.util.LinkedList;

@Getter
@Setter
public class TraceThreadContext {
    public static final ThreadLocal<TraceThreadContext> threadLocal = new ThreadLocal<>();
    private Deque<MethodInfo> methodInfoStack = new LinkedList<>();

    private MethodInfo mainMethodInfo;

    private TraceThreadContext() {
        super();
    }

    /**
     * 获取当前context
     */
    public static TraceThreadContext getContext() {
        TraceThreadContext traceThreadContext = threadLocal.get();
        if (null == traceThreadContext) {
            traceThreadContext = new TraceThreadContext();
            threadLocal.set(traceThreadContext);
        }
        return traceThreadContext;
    }


    /**
     * 防止线程池造成数据重复使用
     */
    public void remove() {
        if (null != threadLocal.get()) {
            threadLocal.remove();
        }
    }
}
