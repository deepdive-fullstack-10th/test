package org.learn.worker.codeworker.dto;


import static org.learn.worker.codeworker.dto.ExecutionStatus.COMPILE_ERROR;
import static org.learn.worker.codeworker.dto.ExecutionStatus.RUNTIME_ERROR;
import static org.learn.worker.codeworker.dto.ExecutionStatus.UNKNOWN_ERROR;
import static org.learn.worker.codeworker.dto.ExecutionStatus.SUCCESS;
import static org.learn.worker.codeworker.dto.ExecutionStatus.TIMEOUT_ERROR;
import static org.learn.worker.codeworker.dto.ExecutionStatus.UNSUPPORTED_LANGUAGE;

public record ExecutionResult(
        String executionId,
        ExecutionStatus status,
        String output,
        long executionTime,
        long memoryUsageKB
) {

    public static ExecutionResult createUnsupportedMessage(String executionId) {
        return new ExecutionResult(executionId, UNSUPPORTED_LANGUAGE, "지원하지 않는 언어입니다", 0, 0);
    }

    public static ExecutionResult createTimeOutError(String executionId) {
        return new ExecutionResult(executionId, TIMEOUT_ERROR, "실행 시간이 초과되었습니다.", 0, 0);
    }

    public static ExecutionResult createSuccessMessage(String executionId, ParseResult res) {
        return new ExecutionResult(executionId, SUCCESS, res.output(), res.time(), res.memory());
    }

    public static ExecutionResult createErrorMessage(String executionId, String error) {
        return new ExecutionResult(executionId, UNKNOWN_ERROR, error, 0, 0);
    }

    public static ExecutionResult createRuntimeError(String executionId, String output) {
        return new ExecutionResult(executionId, RUNTIME_ERROR, output, 0, 0);
    }

    public static ExecutionResult createCompileError(String executionId, String output) {
        return new ExecutionResult(executionId, COMPILE_ERROR, output, 0, 0);
    }

}
