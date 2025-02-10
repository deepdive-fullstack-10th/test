package org.learn.worker.codeworker.dto;


import static org.learn.worker.codeworker.dto.ExecutionStatus.ERROR;
import static org.learn.worker.codeworker.dto.ExecutionStatus.OOM;
import static org.learn.worker.codeworker.dto.ExecutionStatus.SUCCESS;
import static org.learn.worker.codeworker.dto.ExecutionStatus.TIMEOUT;

import org.learn.worker.codeworker.docker.ExecutionMetrics;

public record ExecutionResult(
        String executionId,
        ExecutionStatus status,
        String output,
        long executionTime,
        long memoryUsageKB
) {

    public static ExecutionResult createUnsupportedMessage(String executionId) {
        return new ExecutionResult(executionId, ERROR, "지원하지 않는 언어입니다", 0, 0);
    }

    public static ExecutionResult createTimeOutMessage(String executionId) {
        return new ExecutionResult(executionId, TIMEOUT, "실행 시간이 초과되었습니다.", 0, 0);
    }

    public static ExecutionResult createFailMessage(String executionId, String result, ExecutionMetrics metrics) {
        return new ExecutionResult(executionId, ERROR, result, metrics.executionTime(), metrics.memoryUsage());
    }

    public static ExecutionResult createSuccessMessage(String executionId, String result, ExecutionMetrics metrics) {
        return new ExecutionResult(executionId, SUCCESS, result, metrics.executionTime(), metrics.memoryUsage());
    }

    public static ExecutionResult createErrorMessage(String executionId, String error) {
        return new ExecutionResult(executionId, ERROR, error, 0, 0);
    }

}
