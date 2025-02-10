package org.learn.worker.codeworker.docker;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.learn.worker.codeworker.dto.ExecutionResult;

@Slf4j
public class DockerExecutor {

    public static ExecutionResult executeDockerCommand(
            List<String> command,
             String executionId,
             int timeoutSeconds
    ) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        boolean finishedInTime = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);

        if (!finishedInTime) {
            log.warn("Process timed out: executionId={}, command={}", executionId, String.join(" ", command));
            process.destroyForcibly();
            return ExecutionResult.createTimeOutMessage(executionId);
        }

        String result = new String(process.getInputStream().readAllBytes());
        int exitCode = process.exitValue();
        if (exitCode != 0) {
            return ExecutionResult.createFailMessage(executionId, result);
        }
        return ExecutionResult.createSuccessMessage(executionId, result);
    }

}
