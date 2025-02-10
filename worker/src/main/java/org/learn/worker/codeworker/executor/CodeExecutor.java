package org.learn.worker.codeworker.executor;

import org.learn.worker.codeworker.dto.CodeExecutionMessage;
import org.learn.worker.codeworker.dto.ExecutionResult;

public interface CodeExecutor {

    ExecutionResult execute(CodeExecutionMessage message);

}
