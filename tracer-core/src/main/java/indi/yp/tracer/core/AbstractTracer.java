package indi.yp.tracer.core;

import indi.yp.tracer.core.context.MethodInfo;
import indi.yp.tracer.core.context.SimpleTraceModel;
import indi.yp.tracer.core.context.TraceThreadContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.ArrayList;
import java.util.Deque;

@Slf4j
public abstract class AbstractTracer {

    /**
     * 业务切点,自己实现
     */
    abstract protected void tracePointCut();


    @Before(value = "tracePointCut()")
    public void pushStackInBean(JoinPoint joinPoint) {
        pushStack(joinPoint);
    }

    @AfterReturning(value = "tracePointCut()", returning = "returnValue")
    public void popStackInBean(Object returnValue) {
        boolean traceEnd = popStack(returnValue);
        if (traceEnd) {
            log.info("method trace:{}", SimpleTraceModel.fromMethodInfo(TraceThreadContext.getContext().getMainMethodInfo()));
            TraceThreadContext.getContext().remove();
        }
    }

    private void pushStack(JoinPoint joinPoint) {
        MethodInfo m = new MethodInfo();
        m.setCurrentThreadName(Thread.currentThread().getName());
        Signature signature = joinPoint.getSignature();
        String[] packageArray = signature.getDeclaringTypeName().split("\\.");
        String className = packageArray[packageArray.length - 1];
        m.setMethodName(className + "." + signature.getName());
        m.setStartTime(System.currentTimeMillis());
        Deque<MethodInfo> methodInfoStack = TraceThreadContext.getContext().getMethodInfoStack();
        methodInfoStack.push(m);
    }


    /**
     * 方法出栈
     *
     * @return true: 链路走完 false，链路还没完
     */
    private boolean popStack(Object output) {
        MethodInfo childMethod = TraceThreadContext.getContext().getMethodInfoStack().pop();
        childMethod.setEndTime(System.currentTimeMillis());
        childMethod.setTimeInMs(childMethod.getEndTime() - childMethod.getStartTime());
        if (TraceThreadContext.getContext().getMethodInfoStack().isEmpty()) {
            TraceThreadContext.getContext().setMainMethodInfo(childMethod);
            return true;
        } else {
            MethodInfo parentMethod = TraceThreadContext.getContext().getMethodInfoStack().peek();
            addChildMethod(childMethod, parentMethod);
            return false;
        }
    }

    private void addChildMethod(MethodInfo childMethod, MethodInfo parentMethod) {
        if (parentMethod != null) {
            if (parentMethod.getMethodList() == null) {
                parentMethod.setMethodList(new ArrayList<>());
            }
            parentMethod.getMethodList().add(childMethod);
        }
    }


}
