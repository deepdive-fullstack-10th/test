package org.learn.users;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal Long userId) {
        User user = userRepository.findById(userId).get();
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getNickname(), user.getProfileImageUrl()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AllUserResponse>> getUsers(@AuthenticationPrincipal Long userId) {
        List<User> users = userRepository.findAll();
        List<AllUserResponse> usersResponse = users.stream()
                .map(user -> {
                    boolean isMe = user.getId().equals(userId);
                    return new AllUserResponse(user.getId(), user.getNickname(), user.getProfileImageUrl(), isMe);
                })
                .toList();
        return ResponseEntity.ok(usersResponse);
    }

}
