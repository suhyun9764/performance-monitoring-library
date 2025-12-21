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
        return TraceRepository.getTrace();
    }

    public void end(){
       TraceRepository.endTrace();
    }
}
