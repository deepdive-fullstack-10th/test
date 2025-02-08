package org.learn.worker.codeworker.dto;


import java.time.LocalDateTime;

public record ExecutionResult(
        String executionId,
        ExecutionStatus status,
        String output,
        String errorMessage,
        LocalDateTime completedAt
) {
}
