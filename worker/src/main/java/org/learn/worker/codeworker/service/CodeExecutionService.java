package org.learn.worker.codeworker.service;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.learn.worker.codeworker.dto.CodeExecutionMessage;
import org.learn.worker.codeworker.dto.ExecutionResult;
import org.learn.worker.codeworker.executor.CodeExecutor;
import org.learn.worker.codeworker.executor.impl.JavaCodeExecutor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CodeExecutionService {

    private final Map<String, CodeExecutor> executors = new HashMap<>();

    public CodeExecutionService() {
        executors.put("java", new JavaCodeExecutor());
        // 다른 언어 추가
    }

    public ExecutionResult execute(CodeExecutionMessage message) {
        CodeExecutor executor = executors.get(message.language().toLowerCase());
        if (executor == null) {
            return ExecutionResult.createUnsupportedMessage(message.executionId());
        }
        return executor.execute(message);
    }

}
