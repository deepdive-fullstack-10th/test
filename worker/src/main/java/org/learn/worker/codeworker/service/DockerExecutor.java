package org.learn.worker.codeworker.service;

import static org.learn.worker.codeworker.utils.CodeFileManager.readFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.learn.worker.codeworker.docker.DockerConstant;
import org.learn.worker.codeworker.dto.ExecutionResult;
import org.learn.worker.codeworker.dto.ParseResult;
import org.learn.worker.codeworker.utils.OutputParser;

@Slf4j
public class DockerExecutor {

    public static ExecutionResult execute(
            List<String> command,
            String executionId,
            Path tempDir
    ) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        Process process = pb.start();
        int exitCode = process.waitFor();

        return switch (exitCode) {
            case DockerConstant.COMPILE_ERROR_EXIT_CODE -> processCompileError(executionId, tempDir);
            case DockerConstant.TIMEOUT_EXIT_CODE -> ExecutionResult.createTimeOutError(executionId);
            case DockerConstant.RUNTIME_ERROR_EXIT_CODE -> processRuntimeError(executionId, tempDir);
            case DockerConstant.SUCCESS_OUT_CODE -> processSuccess(executionId, tempDir);
            default -> throw new IllegalStateException("알 수 없는 오류가 발생했습니다 코드 번호: " + exitCode);
        };
    }

    private static ExecutionResult processCompileError(String executionId, Path tempDir) {
        String output = readFile(tempDir, DockerConstant.COMPILE_EXECUTION_LOG).trim();
        log.error("COMPILE_ERROR_OUTPUT: {}", output);
        return ExecutionResult.createCompileError(executionId, output);
    }

    private static ExecutionResult processRuntimeError(String executionId, Path tempDir) {
        String output = readFile(tempDir, DockerConstant.EXECUTION_RESULT_LOG).trim();
        ParseResult parseResult = OutputParser.parse(output);
        return ExecutionResult.createRuntimeError(executionId, parseResult.output());
    }

    private static ExecutionResult processSuccess(String executionId, Path tempDir) {
        String output = readFile(tempDir, DockerConstant.EXECUTION_RESULT_LOG).trim();
        ParseResult parseResult = OutputParser.parse(output);
        log.error("SUCCESS_EXIT_CODE: {}", executionId);
        log.error("SUCCESS_OUTPUT: {}", output);
        return ExecutionResult.createSuccessMessage(executionId, parseResult);
    }

}
