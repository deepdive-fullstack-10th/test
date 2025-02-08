package org.learn.worker.codeworker.dto;


import static java.time.LocalDateTime.now;
import static org.learn.worker.codeworker.dto.ExecutionStatus.FAIL;
import static org.learn.worker.codeworker.dto.ExecutionStatus.SUCCESS;

import java.time.LocalDateTime;

public record ExecutionResult(
        String executionId,
        ExecutionStatus status,
        String output,
        String error,
        LocalDateTime completedAt
) {

    public static ExecutionResult createUnsupportedMessage(String executionId) {
        return new ExecutionResult(executionId, FAIL, null, "Java만 지원합니다", now());
    }

    public static ExecutionResult createTimeOutMessage(String executionId) {
        return new ExecutionResult(executionId, FAIL, null, "실행 시간이 초과되었습니다 (1초)", now());
    }

    public static ExecutionResult createFailMessage(String executionId, String result) {
        return new ExecutionResult(executionId, FAIL, null, result, now());
    }

    public static ExecutionResult createSuccessMessage(String executionId, String result) {
        return new ExecutionResult(executionId, SUCCESS, result, null, now());
    }

    public static ExecutionResult createErrorMessage(String executionId, String error) {
        return new ExecutionResult(executionId, FAIL, null, error, now());
    }

}
