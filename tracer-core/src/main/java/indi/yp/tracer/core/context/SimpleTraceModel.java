package indi.yp.tracer.core.context;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import indi.yp.tracer.core.constant.CommonConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 用于将最终信息简化
 */
@Getter
@Setter
@Slf4j
public class SimpleTraceModel {
    /**
     * cost,method,thread
     */
    private static final String DESC_FORMAT = "%sms,%s,%s";
    private String desc;
    @JSONField(ordinal = 1)
    private List<SimpleTraceModel> child;
    @JSONField(ordinal = 2)
    private List<SimpleTraceModel> asyncChild;

    public static String fromMethodInfo(MethodInfo methodInfo) {
        try {
            if (null == methodInfo) {
                return CommonConstants.EMPTY;
            }
            log.debug("origin trace methodInfo :{}", JSONObject.toJSONString(methodInfo));
            SimpleTraceModel simpleTraceModel = process(methodInfo);
            return JSONObject.toJSONString(simpleTraceModel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return CommonConstants.EMPTY;
    }

    public static SimpleTraceModel process(MethodInfo methodInfo) {
        SimpleTraceModel simpleTraceModel = new SimpleTraceModel();
        String desc = String.format(DESC_FORMAT, methodInfo.getTimeInMs(), methodInfo.getMethodName(),
                methodInfo.getCurrentThreadName());
        simpleTraceModel.setDesc(desc);
        List<MethodInfo> methodInfos = methodInfo.getMethodList();
        if (!CollectionUtils.isEmpty(methodInfos)) {
            List<SimpleTraceModel> child = new ArrayList<>();
            for (MethodInfo info : methodInfos) {
                child.add(process(info));
            }
            simpleTraceModel.setChild(child);
        }

        Deque<MethodInfo> asyncMethodInfoList = methodInfo.getAsyncContext();
        if (!CollectionUtils.isEmpty(asyncMethodInfoList)) {
            List<SimpleTraceModel> asyncChild = new ArrayList<>();
            for (MethodInfo asyncMethodInfo : asyncMethodInfoList) {
                asyncChild.add(process(asyncMethodInfo));
            }
            simpleTraceModel.setAsyncChild(asyncChild);
        }
        return simpleTraceModel;
    }
}
