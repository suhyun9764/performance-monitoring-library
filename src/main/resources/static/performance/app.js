window.addEventListener('DOMContentLoaded', () => {
    refreshData();
    // 5초마다 자동 새로고침
    setInterval(refreshData, 5000);
});

async function refreshData() {
    try {
        // 메트릭 데이터
        const metricsResponse = await fetch('/performance/api/metrics');
        const requests = await metricsResponse.json();

        // Tomcat 스레드 풀 상태
        const poolResponse = await fetch('/performance/api/thread-pool');
        const threadPool = await poolResponse.json();

        updateStats(requests);
        updateThreadPool(threadPool);
        updateRequestsList(requests);

    } catch (error) {
        console.error('Failed to load data:', error);
    }
}

async function clearMetrics() {
    if (!confirm('모든 메트릭을 삭제하시겠습니까?')) {
        return;
    }

    try {
        await fetch('/performance/api/clear');
        refreshData();
    } catch (error) {
        console.error('Failed to clear metrics:', error);
    }
}

/**
 * 성능 통계 업데이트
 */
function updateStats(requests) {
    const totalRequests = requests.length;
    document.getElementById('total-requests').textContent = totalRequests;

    if (totalRequests === 0) {
        document.getElementById('avg-time').textContent = '0ms';
        document.getElementById('slowest-time').textContent = '0ms';
        document.getElementById('fastest-time').textContent = '0ms';
        return;
    }

    const times = requests.map(r => r.totalExecutionTime);
    const avgTime = Math.round(times.reduce((a, b) => a + b, 0) / times.length);
    const slowestTime = Math.max(...times);
    const fastestTime = Math.min(...times);

    document.getElementById('avg-time').textContent = avgTime + 'ms';
    document.getElementById('slowest-time').textContent = slowestTime + 'ms';
    document.getElementById('fastest-time').textContent = fastestTime + 'ms';
}

/**
 * Tomcat 스레드 풀 업데이트
 */
function updateThreadPool(stats) {
    // 스레드 상태
    document.getElementById('current-threads').textContent = stats.currentThreads;
    document.getElementById('max-threads').textContent = stats.maxThreads;
    document.getElementById('active-threads').textContent = stats.activeThreads;

    // 연결 상태
    document.getElementById('connections').textContent = formatNumber(stats.connectionCount);
    document.getElementById('max-connections').textContent = formatNumber(stats.maxConnections);

    // 활성률
    document.getElementById('active-rate').textContent = stats.threadActiveRate.toFixed(1);

    // 프로그레스 바
    updateProgressBar('thread-utilization', stats.threadUtilization);
    updateProgressBar('connection-utilization', stats.connectionUtilization);
    updateProgressBar('active-rate-bar', stats.threadActiveRate);
}

/**
 * 요청 목록 업데이트
 */
function updateRequestsList(requests) {
    const container = document.getElementById('requests-list');

    if (requests.length === 0) {
        container.innerHTML = '<div class="empty-state">아직 수집된 메트릭이 없습니다</div>';
        return;
    }

    // 최신순 정렬
    requests.sort((a, b) => {
        const timeA = new Date(a.metrics[0]?.timestamp || 0);
        const timeB = new Date(b.metrics[0]?.timestamp || 0);
        return timeB - timeA;
    });

    container.innerHTML = requests.map(request => createRequestItem(request)).join('');
}

/**
 * 요청 아이템 생성
 */
function createRequestItem(request) {
    const url = request.requestUrl || 'UNKNOWN';
    const [method, ...pathParts] = url.split(' ');
    const path = pathParts.join(' ') || '/';
    const totalTime = request.totalExecutionTime || 0;
    const timeClass = getTimeClass(totalTime);

    return `
        <div class="request-item">
            <div class="request-header" onclick="toggleDetails('${request.traceId}')">
                <div class="request-info">
                    <span class="method-badge method-${method.toLowerCase()}">${method}</span>
                    <span class="request-path">${path}</span>
                </div>
                <span class="request-time ${timeClass}">${totalTime}ms</span>
            </div>
            <div class="request-details" id="details-${request.traceId}" style="display: none;">
                ${createMetricsTree(request.metrics)}
            </div>
        </div>
    `;
}

/**
 * 메트릭 트리 생성
 */
function createMetricsTree(metrics) {
    if (!metrics || metrics.length === 0) {
        return '<div class="metric-empty">메트릭이 없습니다</div>';
    }

    // depth로 정렬
    const sorted = [...metrics].sort((a, b) => a.depth - b.depth);

    return sorted.map(metric => {
        const indent = metric.depth * 20;
        const selfTime = metric.selfExecutionTime || 0;
        const timeClass = getTimeClass(selfTime);

        return `
            <div class="metric-row" style="padding-left: ${indent}px">
                <div class="metric-info">
                    <span class="layer-badge layer-${metric.layer.toLowerCase()}">${metric.layer}</span>
                    <span class="metric-method">${metric.className}.${metric.methodName}()</span>
                </div>
                <span class="metric-time ${timeClass}">${selfTime}ms</span>
            </div>
        `;
    }).join('');
}

/**
 * 상세 토글
 */
function toggleDetails(traceId) {
    const details = document.getElementById('details-' + traceId);
    if (details.style.display === 'none') {
        details.style.display = 'block';
    } else {
        details.style.display = 'none';
    }
}

/**
 * 프로그레스 바 업데이트
 */
function updateProgressBar(elementId, percentage) {
    const element = document.getElementById(elementId);
    element.style.width = Math.min(percentage, 100) + '%';
    element.className = 'progress-fill ' + getProgressClass(percentage);
}

/**
 * 시간 클래스 반환
 */
function getTimeClass(time) {
    if (time < 50) return 'time-fast';
    if (time < 200) return 'time-normal';
    return 'time-slow';
}

/**
 * 프로그레스 클래스 반환
 */
function getProgressClass(percentage) {
    if (percentage >= 90) return 'progress-danger';
    if (percentage >= 70) return 'progress-warning';
    return 'progress-success';
}

/**
 * 숫자 포맷
 */
function formatNumber(num) {
    return num.toLocaleString('ko-KR');
}