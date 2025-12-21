package com.suhyun.performancestarter.aop;

import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.event.SimpleJdbcEventListener;
import com.suhyun.performancestarter.aop.manager.QueryManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnClass(name = "com.p6spy.engine.spy.P6SpyFactory")
@RequiredArgsConstructor
public class QueryInterceptor extends SimpleJdbcEventListener {

    private final QueryManager queryManager;

    @Override
    public void onBeforeAnyExecute(StatementInformation statementInformation) {
        String sql = statementInformation.getSql();

        if (sql != null && !sql.trim().isEmpty()) {
            queryManager.addQuery(sql);
        }
    }
}