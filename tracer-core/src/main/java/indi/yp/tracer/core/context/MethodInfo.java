package indi.yp.tracer.core.context;

import lombok.Data;

import java.util.Deque;
import java.util.List;

@Data
public class MethodInfo {
    private String methodName;
    private List<MethodInfo> methodList;
    private Long timeInMs;
    private Long startTime;
    private Long endTime;
    private String currentThreadName;
    private Deque<MethodInfo> asyncContext;


}
