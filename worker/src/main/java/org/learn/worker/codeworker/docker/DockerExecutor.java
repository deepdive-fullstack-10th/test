package org.learn.worker.codeworker.docker;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.learn.worker.codeworker.dto.ExecutionResult;

@Slf4j
public class DockerExecutor {

    private static final int TIMEOUT_EXIT_CODE = 124;

    public static ExecutionResult executeDockerCommand(
            List<String> command,
            String executionId
    ) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(false);

        Process process = pb.start();
        int exitCode = process.waitFor();
        String successFullOutput = new String(process.getInputStream().readAllBytes());
        String errorOutput = new String(process.getErrorStream().readAllBytes());

        if (exitCode == TIMEOUT_EXIT_CODE) {
            log.error("error Output: {}", errorOutput);
            return ExecutionResult.createTimeOutMessage(executionId);
        }

        ExecutionMetrics metrics = ExecutionMetrics.parseMetrics(successFullOutput);
        String programOutput = ExecutionMetrics.extractProgramOutput(successFullOutput);

        if (exitCode != 0 || metrics.errorLog() != null) {
            log.error("Exit Code: {}, JVM Error Log: {}", exitCode, metrics.errorLog());
            return ExecutionResult.createFailMessage(executionId, metrics.errorLog(), metrics);
        }

        log.info("success Output: {}", successFullOutput);
        return ExecutionResult.createSuccessMessage(executionId, programOutput, metrics);
    }

}
