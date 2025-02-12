package org.learn.worker.codeworker.docker;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class DockerCommander {

    private final List<String> commands = new ArrayList<>();

    public static DockerCommander builder() {
        return new DockerCommander().add("docker");
    }

    public DockerCommander add(String... args) {
        commands.addAll(List.of(args));
        return this;
    }

    public DockerCommander withRun() {
        return add("run");
    }

    public DockerCommander withVolume(Path tempDir) {
        return add("-v", tempDir.toAbsolutePath() + ":/code");
    }

    public DockerCommander withWorkDir(String workDir) {
        return add("-w", workDir);
    }

    public DockerCommander withLimits(String memory, String cpus) {
        return add("--memory=" + memory, "--cpus=" + cpus);
    }

    public DockerCommander withSecurity() {
        return add("--network=none");
    }

    public DockerCommander withName(String containerId) {
        return add("--name", containerId);
    }

    public DockerCommander withImage(String image) {
        return add(image);
    }

    public DockerCommander withCommand(String command) {
        return add("sh", "-c", command);
    }

    public DockerCommander withRemove() {
        return add("--rm");
    }

    public List<String> build() {
        return commands;
    }

}
