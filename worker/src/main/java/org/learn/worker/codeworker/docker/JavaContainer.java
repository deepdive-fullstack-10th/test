package org.learn.worker.codeworker.docker;

import java.nio.file.Path;
import java.util.List;

public class JavaContainer {

    private static final String DEFAULT_MEMORY = "128m";
    private static final String DEFAULT_CPUS = "1";
    private static final String DEFAULT_WORK_DIR = "/code";
    private static final String DEFAULT_JAVA_IMAGE = "openjdk:17-slim";
    private static final String DEFAULT_COMMAND_FORMAT = "echo '%s' | javac Main.java && echo '%s' | java Main";

    public static List<String> extract(String containerId, Path tempDir, String command) {
        return DockerCommander.builder()
                .withRun()
                .withRemove()
                .withLimits(DEFAULT_MEMORY, DEFAULT_CPUS)
                .withSecurity()
                .withName(containerId)
                .withVolume(tempDir)
                .withWorkDir(DEFAULT_WORK_DIR)
                .withImage(DEFAULT_JAVA_IMAGE)
                .withCommand(command)
                .build();
    }

    public static String extractCommand(String input) {
        return String.format(DEFAULT_COMMAND_FORMAT, input, input);
    }

}
