package org.learn.worker.codeworker.executor.impl;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.learn.worker.codeworker.docker.DockerExecutor;
import org.learn.worker.codeworker.docker.JavaContainer;
import org.learn.worker.codeworker.dto.CodeExecutionMessage;
import org.learn.worker.codeworker.dto.ExecutionResult;
import org.learn.worker.codeworker.executor.CodeExecutor;
import org.learn.worker.codeworker.utils.CodeFileManager;
import org.learn.worker.codeworker.utils.TempDirectoryManager;

@Slf4j
public class JavaCodeExecutor implements CodeExecutor {

    @Override
    public ExecutionResult execute(CodeExecutionMessage message) {
        String containerId = "java-exec-" + UUID.randomUUID();
        Path tempDir = null;

        try {
            tempDir = TempDirectoryManager.createTempDirectory();
            CodeFileManager.createJavaFile(tempDir, message.code());
            List<String> dockerCommand = JavaContainer.extract(containerId, tempDir, message.input().trim());
            log.info("Executing code in container: {}", containerId);

            return DockerExecutor.executeDockerCommand(dockerCommand, message.executionId());
        } catch (Exception e) {
            return ExecutionResult.createErrorMessage(message.executionId(), e.getMessage());
        } finally {
            TempDirectoryManager.cleanup(tempDir);
        }
    }

}
