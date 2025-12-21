package com.suhyun.performancestarter.aop.manager;

import com.suhyun.performancestarter.aop.repository.QueryRepository;
import com.suhyun.performancestarter.model.QueryStatistics;
import org.springframework.stereotype.Component;

@Component
public class QueryManager {

    public void activateMonitoring(String methodId) {
        QueryRepository.activateMonitoring(methodId);
    }

    /**
     * 모니터링 종료
     */
    public void deactivateMonitoring() {
        QueryRepository.deactivateMonitoring();
    }

    public void addQuery(String sql) {
        QueryRepository.addQuery(sql);
    }

    public QueryStatistics getStatistics(String methodId) {
        return QueryRepository.getStatistics(methodId);
    }

    public void end() {
        QueryRepository.clearAll();
    }
}
