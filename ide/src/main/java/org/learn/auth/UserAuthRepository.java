package org.learn.auth;

import org.learn.auth.domain.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuth, String> {
}
