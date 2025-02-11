package org.learn.worker.codeworker.utils;

import lombok.extern.slf4j.Slf4j;
import org.learn.worker.codeworker.dto.ParseResult;

@Slf4j
public class OutputParser {

    public static ParseResult parse(String output) {
        String[] lines = output.split("\n");
        long time = 0;
        long memory = 0;

        try {
            double seconds = Double.parseDouble(lines[lines.length - 2]);
            time = Math.round(seconds * 1000);
            memory = Long.parseLong(lines[lines.length - 1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            log.error("알 수 없는 이유로 파싱 실패");
        }

        StringBuilder outputBuilder = new StringBuilder();
        for (int i = 0; i < lines.length - 2; i++) {
            outputBuilder.append(lines[i]);
            if (i < lines.length - 3) {
                outputBuilder.append("\n");
            }
        }

        return new ParseResult(outputBuilder.toString(), time, memory);
    }

}
