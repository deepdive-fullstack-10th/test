package org.learn.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @RequestMapping("/me")
    public ResponseEntity<UserResponse> getUser(@RequestParam Long userId) {
        User user = userRepository.findById(userId).get();
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getNickname(), user.getProfileImageUrl()));
    }

}
