package org.learn.worker.codeworker.docker;

public final class DockerConstant {

    public static final String DEFAULT_MEMORY = "128m";
    public static final String DEFAULT_CPUS = "1";
    public static final String DEFAULT_WORK_DIR = "/code";

    public static final String JAVA_IMAGE = "java-code-executor";
    public static final String JAVA_COMPILE_OPTIONS =
            "-J-Xms64m -J-Xmx128m -J-Xss256m -encoding UTF-8";
    public static final String JAVA_RUNTIME_OPTIONS =
            "-Xms64m -Xmx128m -Xss256m -Dfile.encoding=UTF-8 -XX:+UseSerialGC";
    public static final String JAVA_COMMAND_FORMAT = """
    javac %s Main.java && \
    java %s -XX:+PrintGCDetails -XX:+PrintGC -XX:+UnlockDiagnosticVMOptions -XX:+LogCompilation -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/code/heapdump.hprof Main 2> /code/error.log || true && \
    echo "=====JVM OUTPUT=====" && \
    cat /code/error.log && \
    echo "=====TIME=====" && \
    timeout 2 /usr/bin/time -f "%%e\\n%%M" java %s Main 2> time_output.txt && \
    echo "=====MEMORY=====" && \
    tail -n 1 time_output.txt
    """;

}
