package org.learn.worker.codeworker.docker.container;

import static org.learn.worker.codeworker.docker.DockerConstant.DEFAULT_CPUS;
import static org.learn.worker.codeworker.docker.DockerConstant.DEFAULT_MEMORY;
import static org.learn.worker.codeworker.docker.DockerConstant.DEFAULT_WORK_DIR;

import java.nio.file.Path;
import java.util.List;
import org.learn.worker.codeworker.docker.DockerCommander;

public class NodeContainer {

    private static final String NODE_IMAGE = "js-code-executor";
    private static final String NODE_COMMAND_FORMAT = """
        # Syntax check
        compilation_output=$(node --check Main.js 2>&1); \
        compilation_status=$?; \
        echo "$compilation_output" > compile.log; \
        if [ $compilation_status -ne 0 ]; then \
            exit 1; \
        fi && \
        
        # Execution
        { \
            output=$(echo '%s' | timeout 2 /usr/bin/time -f "%%e\\n%%M" node Main.js 2>&1); \
            exit_code=$?; \
            echo "$output" > output.log; \
            if [ $exit_code -eq 124 ]; then \
                exit 124; \
            elif [ $exit_code -ne 0 ]; then \
                exit 2; \
            fi \
        }
        """;

    public static List<String> generate(String containerId, Path tempDir, String input) {
        return DockerCommander.builder()
                .withRun()
                .withRemove()
                .withLimits(DEFAULT_MEMORY, DEFAULT_CPUS)
                .withSecurity()
                .withName(containerId)
                .withVolume(tempDir)
                .withWorkDir(DEFAULT_WORK_DIR)
                .withImage(NODE_IMAGE)
                .withCommand(String.format(NODE_COMMAND_FORMAT, input))
                .build();
    }
}