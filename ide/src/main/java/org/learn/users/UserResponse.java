package org.learn.users;

public record UserResponse(
        Long id,
        String nickname,
        String profileImageUrl
) {
}
