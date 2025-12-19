package com.suhyun.performancestarter.aop.utils;

import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.event.SimpleJdbcEventListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnClass(name = "com.p6spy.engine.spy.P6SpyFactory")
public class QueryInterceptor extends SimpleJdbcEventListener {
    
    private final QueryCounter queryCounter;
    
    public QueryInterceptor(QueryCounter queryCounter) {
        this.queryCounter = queryCounter;
    }

    @Override
    public void onBeforeAnyExecute(StatementInformation statementInformation) {
        String sql = statementInformation.getSql();

        if(sql != null && !sql.trim().isEmpty()){
            queryCounter.incrementQuery(sql);
        }
    }
}