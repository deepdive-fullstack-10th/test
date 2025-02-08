package org.learn.worker.codeworker.service;

import lombok.extern.slf4j.Slf4j;
import org.learn.worker.codeworker.dto.CodeExecutionMessage;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CodeExecutionService {

    public void execute(CodeExecutionMessage message) {
        log.info("{} 코드 실행!!", message.language());

        // TODO: 실제 코드 실행 로직 구현
    }

}
