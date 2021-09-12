package indi.yp.tracer.demo.config;

import indi.yp.tracer.core.AbstractTracer;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class TraceConfig extends AbstractTracer {
    @Override
    @Pointcut(value = "execution(* indi.yp.tracer.demo..*.*(..))")
    protected void tracePointCut() {
    }
}
