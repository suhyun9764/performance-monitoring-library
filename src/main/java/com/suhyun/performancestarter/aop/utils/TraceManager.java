package com.suhyun.performancestarter.aop.utils;

import com.suhyun.performancestarter.aop.context.TraceContext;
import com.suhyun.performancestarter.aop.dto.TraceInfo;
import org.springframework.stereotype.Component;

@Component
public class TraceManager {

    public TraceInfo start(){
        String traceId = TraceContext.getCurrentTraceId();
        int depth = TraceContext.increaseDepth();
        boolean isRootCall = (depth==0);

        return TraceInfo.builder()
                .traceId(traceId)
                .depth(depth)
                .isRootCall(isRootCall)
                .build();
    }

    public void end(TraceInfo traceInfo){
        TraceContext.decreaseDepth();
        if(traceInfo.isRootCall())
            TraceContext.endTrace();;
    }
}
