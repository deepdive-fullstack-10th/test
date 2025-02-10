package org.learn.worker.codeworker.docker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ExecutionMetrics(long executionTime, long memoryUsage, String errorLog) {

    private static final Pattern TIME_PATTERN = Pattern.compile("=====TIME=====\\s*([0-9.]+)");
    private static final Pattern MEMORY_PATTERN = Pattern.compile("=====MEMORY=====\\s*([0-9]+)");
    private static final Pattern JVM_OUTPUT_PATTERN = Pattern.compile("=====JVM OUTPUT=====\\s*(.+?)\\s*=====TIME=====", Pattern.DOTALL);

    private static String cleanWhitespace(String input) {
        if (input == null) return null;
        return input.replaceAll("\\s+", " ").trim();
    }

    public static ExecutionMetrics parseMetrics(String output) {
        String errorLog = extractErrorLog(output);
        long executionTime = extractExecutionTime(output);
        long memoryUsage = extractMemoryUsage(output);

        return new ExecutionMetrics(executionTime, memoryUsage, cleanWhitespace(errorLog));
    }

    private static long extractMemoryUsage(String output) {
        Matcher memoryMatcher = MEMORY_PATTERN.matcher(output);
        if (memoryMatcher.find()) {
            return Long.parseLong(memoryMatcher.group(1));
        }
        return 0;
    }

    private static long extractExecutionTime(String output) {
        Matcher timeMatcher = TIME_PATTERN.matcher(output);
        if (timeMatcher.find()) {
            String timeStr = timeMatcher.group(1);
            double seconds = Double.parseDouble(timeStr);
            return Math.round(seconds * 1000);
        }
        return 0;
    }

    private static String extractErrorLog(String output) {
        Matcher jvmOutputMatcher = JVM_OUTPUT_PATTERN.matcher(output);
        if (jvmOutputMatcher.find()) {
            return jvmOutputMatcher.group(1).trim();
        }
        return null;
    }

    public static String extractProgramOutput(String result) {
        int jvmOutputIndex = result.indexOf("=====JVM OUTPUT=====");
        if (jvmOutputIndex != -1) {
            result = result.substring(0, jvmOutputIndex);
        }

        int timeIndex = result.indexOf("=====TIME=====");
        if (timeIndex == -1) return result;
        return cleanWhitespace(result.substring(0, timeIndex));
    }

}