package org.learn.users;

public record AllUserResponse(
        Long id,
        String nickname,
        String profileImageUrl,
        boolean userIsMe
) {
}
