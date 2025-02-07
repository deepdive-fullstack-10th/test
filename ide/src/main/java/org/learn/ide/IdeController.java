package org.learn.ide;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ide")
public class IdeController {

    @PostMapping("/run")
    public ResponseEntity<IdeRunResponse> run(@AuthenticationPrincipal Long userId, @RequestBody IdeRequest request) {
        return ResponseEntity.ok(new IdeRunResponse(userId, "Success"));
    }

    public record IdeRequest(String language, String code, String input) {}

    public record IdeRunResponse(Long userId, String result) {}

}
