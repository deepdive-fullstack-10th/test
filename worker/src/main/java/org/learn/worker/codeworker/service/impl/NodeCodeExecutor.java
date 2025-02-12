package org.learn.worker.codeworker.service.impl;

import static org.learn.worker.codeworker.utils.CodeFileManager.createNodeFile;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.learn.worker.codeworker.docker.container.NodeContainer;
import org.learn.worker.codeworker.dto.CodeExecutionMessage;
import org.learn.worker.codeworker.dto.ExecutionResult;
import org.learn.worker.codeworker.service.CodeExecutor;
import org.learn.worker.codeworker.service.DockerExecutor;
import org.learn.worker.codeworker.utils.TempDirectoryManager;

@Slf4j
public class NodeCodeExecutor implements CodeExecutor {

    @Override
    public ExecutionResult execute(CodeExecutionMessage message) {
        String containerId = "node-exec-" + UUID.randomUUID();
        Path tempDir = null;

        try {
            tempDir = TempDirectoryManager.createTempDirectory();
            createNodeFile(tempDir, message.code());
            List<String> dockerCommand = NodeContainer.generate(containerId, tempDir, message.input().trim());
            log.info("Executing code in container: {}", containerId);

            return DockerExecutor.execute(dockerCommand, message.executionId(), tempDir);
        } catch (Exception e) {
            return ExecutionResult.createErrorMessage(message.executionId(), e.getMessage());
        } finally {
            TempDirectoryManager.cleanup(tempDir);
        }
    }

}
