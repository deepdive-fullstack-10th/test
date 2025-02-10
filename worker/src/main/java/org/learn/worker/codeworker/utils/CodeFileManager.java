package org.learn.worker.codeworker.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CodeFileManager {

    private static void createFile(Path tempDir, String filename, String code) throws IOException {
        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, code);
    }

    public static void createJavaFile(Path tempDir, String code) throws IOException {
        createFile(tempDir, "Main.java", code);
    }

}
