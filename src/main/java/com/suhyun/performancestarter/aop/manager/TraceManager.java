package com.suhyun.performancestarter.aop.manager;

import com.suhyun.performancestarter.aop.dto.TraceInfo;
import com.suhyun.performancestarter.aop.repository.TraceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class TraceManager {

    public TraceInfo start(){
        String traceId = TraceRepository.getCurrentTraceId();
        int currentDepth = TraceRepository.getCurrentDepth();
        boolean isRootCall = (currentDepth == 0);

        String executionId = UUID.randomUUID().toString();
        TraceRepository.increaseDepth();

        return TraceInfo.builder()
                .traceId(traceId)
                .depth(currentDepth)
                .executionId(executionId)
                .isRootCall(isRootCall)
                .build();
    }

    public void end(TraceInfo traceInfo){
        TraceRepository.decreaseDepth();
        if(traceInfo.isRootCall()) {
            TraceRepository.endTrace();
        }
    }
}
