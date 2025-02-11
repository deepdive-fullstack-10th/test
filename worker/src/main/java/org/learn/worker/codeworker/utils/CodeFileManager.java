package org.learn.worker.codeworker.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeFileManager {

    private static void createFile(Path tempDir, String filename, String code) throws IOException {
        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, code);
    }

    public static void createJavaFile(Path tempDir, String code) throws IOException {
        createFile(tempDir, "Main.java", code);
    }

    public static void createPythonFile(Path tempDir, String code) throws IOException {
        createFile(tempDir, "Main.py", code);
    }

    public static String readFile(Path tempDir, String filename) {
        try {
            Path path = tempDir.resolve(filename);
            return Files.exists(path) ? Files.readString(path) : "";
        } catch (IOException e) {
            log.error("Failed to read file: {}", filename, e);
            return "";
        }
    }

}
