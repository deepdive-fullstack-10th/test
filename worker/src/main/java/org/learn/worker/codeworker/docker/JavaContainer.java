    package org.learn.worker.codeworker.docker;

    import static org.learn.worker.codeworker.docker.DockerConstant.DEFAULT_CPUS;
    import static org.learn.worker.codeworker.docker.DockerConstant.JAVA_COMMAND_FORMAT;
    import static org.learn.worker.codeworker.docker.DockerConstant.JAVA_COMPILE_OPTIONS;
    import static org.learn.worker.codeworker.docker.DockerConstant.JAVA_IMAGE;
    import static org.learn.worker.codeworker.docker.DockerConstant.DEFAULT_MEMORY;
    import static org.learn.worker.codeworker.docker.DockerConstant.DEFAULT_WORK_DIR;
    import static org.learn.worker.codeworker.docker.DockerConstant.JAVA_RUNTIME_OPTIONS;

    import java.nio.file.Path;
    import java.util.List;

    public class JavaContainer {

        public static List<String> extract(String containerId, Path tempDir, String input) {
            return DockerCommander.builder()
                    .withRun()
                    .withRemove()
                    .withLimits(DEFAULT_MEMORY, DEFAULT_CPUS)
                    .withSecurity()
                    .withName(containerId)
                    .withVolume(tempDir)
                    .withWorkDir(DEFAULT_WORK_DIR)
                    .withImage(JAVA_IMAGE)
                    .withCommand(String.format(JAVA_COMMAND_FORMAT, JAVA_COMPILE_OPTIONS, input, JAVA_RUNTIME_OPTIONS))
                    .build();
        }

    }
