package org.learn.worker.codeworker.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.learn.worker.codeworker.dto.CodeExecutionMessage;
import org.learn.worker.codeworker.dto.ExecutionResult;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CodeExecutionService {

    public ExecutionResult execute(CodeExecutionMessage message) {
        if (!"java".equalsIgnoreCase(message.language())) {
            return ExecutionResult.createUnsupportedMessage(message.executionId());
        }

        String containerId = "java-exec-" + UUID.randomUUID();
        Path tempDir = null;

        try {
            // 1. 임시 디렉토리 생성
            tempDir = Files.createTempDirectory("java_exec_");

            // 2. Java 파일 생성
            String className = "Main";
            Path sourcePath = tempDir.resolve("Main.java");
            Files.writeString(sourcePath, message.code());

            // 3. Docker로 실행
            String format = String.format(
                    "echo '%s' | javac %s.java && echo '%s' | java %s",
                    message.input(),  // 컴파일용 입력 (사실 필요 없지만 일관성을 위해)
                    className,
                    message.input(),  // 실제 실행시 입력으로 전달
                    className
            );

            log.info("java run format: {}", format);
            List<String> dockerCommand = List.of(
                    "docker", "run",
                    "--rm",                     // 실행 후 자동 삭제
                    "--memory=128m",            // 메모리 제한 128MB
                    "--cpus=1",                 // CPU 제한
                    "--network=none",           // 네트워크 격리
                    "--name", containerId,      // 컨테이너 이름
                    "-v", tempDir.toAbsolutePath() + ":/code",  // 소스코드 마운트
                    "-w", "/code",              // 작업 디렉토리
                    "openjdk:17-slim",          // Java 17 이미지
                    "sh", "-c",                 // 실행할 명령어
                    format
            );

            log.info("Executing code in container: {}", containerId);

            // 4. 프로세스 실행
            ProcessBuilder pb = new ProcessBuilder(dockerCommand);
            pb.redirectErrorStream(true);  // 에러를 표준 출력으로 리다이렉트

            Process process = pb.start();

            // 5. 타임아웃 설정 (1초)
            if (!process.waitFor(1, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                return ExecutionResult.createTimeOutMessage(message.executionId());
            }

            // 6. 결과 수집
            String result = new String(process.getInputStream().readAllBytes());

            // 7. 종료 코드 확인
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                return ExecutionResult.createFailMessage(message.executionId(), result);
            }

            log.info("Code execution completed: {}", containerId);
            return ExecutionResult.createSuccessMessage(message.executionId(), result);
        } catch (Exception e) {
            log.error("Code execution failed: {}", containerId, e);
            return ExecutionResult.createErrorMessage(message.executionId(), e.getMessage());
        } finally {
            // 8. 임시 디렉토리 정리
            if (tempDir != null) {
                try {
                    Files.walk(tempDir)
                            .sorted(Comparator.reverseOrder())
                            .forEach(path -> {
                                try {
                                    Files.delete(path);
                                } catch (IOException e) {
                                    log.error("Failed to delete: {}", path, e);
                                }
                            });
                } catch (IOException e) {
                    log.error("Failed to cleanup directory: {}", tempDir, e);
                }
            }
        }
    }

}
