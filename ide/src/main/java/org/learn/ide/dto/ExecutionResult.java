package org.learn.ide.dto;

public record ExecutionResult(
        String executionId,
        String status,
        String output,
        String error,
        long executionTime
) {

    public static ExecutionResult initMessage(String executionId) {
        return new ExecutionResult(executionId, "RUNNING", "코드 실행 중...", null, 0);
    }

}
