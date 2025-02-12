package org.learn.worker.codeworker.service;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.learn.worker.codeworker.dto.CodeExecutionMessage;
import org.learn.worker.codeworker.dto.ExecutionResult;
import org.learn.worker.codeworker.service.impl.CppCodeExecutor;
import org.learn.worker.codeworker.service.impl.JavaCodeExecutor;
import org.learn.worker.codeworker.service.impl.NodeCodeExecutor;
import org.learn.worker.codeworker.service.impl.PythonCodeExecutor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CodeExecutionService {

    private final Map<String, CodeExecutor> executors = new HashMap<>();

    public CodeExecutionService() {
        executors.put("java", new JavaCodeExecutor());
        executors.put("python", new PythonCodeExecutor());
        executors.put("javascript", new NodeCodeExecutor());
        executors.put("cpp", new CppCodeExecutor());
        // 다른 언어 추가
    }

    public ExecutionResult execute(CodeExecutionMessage message) {
        log.info("EXECUTE =======> {}", message);
        CodeExecutor executor = executors.get(message.language().toLowerCase());
        if (executor == null) {
            return ExecutionResult.createUnsupportedMessage(message.executionId());
        }
        return executor.execute(message);
    }

}
