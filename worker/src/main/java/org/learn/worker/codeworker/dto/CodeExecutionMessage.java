package org.learn.worker.codeworker.dto;

import java.time.LocalDateTime;

public record CodeExecutionMessage(
        String executionId,
        String language,
        String code,
        String input,
        LocalDateTime requestedAt
) {
}
