package org.learn.ide.dto;

import java.time.LocalDateTime;
import org.learn.ide.IdeController.IdeRequest;

public record CodeExecutionMessage(
        String executionId,
        String language,
        String code,
        String input,
        LocalDateTime requestedAt
) {

    public static CodeExecutionMessage createNewMessage(IdeRequest request) {
        return new CodeExecutionMessage(
                request.ideId(),
                request.language(),
                request.code(),
                request.input(),
                LocalDateTime.now()
        );
    }

}