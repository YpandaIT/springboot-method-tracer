package indi.yp.tracer.core;


public interface TraceFilter {

    default boolean shouldTrace() {
        return true;
    }

}
