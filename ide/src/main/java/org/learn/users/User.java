package org.learn.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cocomu_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    private static final String DEFAULT_PROFILE_IMAGE = "https://cdn.cocomu.co.kr/images/profile.png";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 8)
    private String nickname;

    @Column(nullable = false)
    private String profileImageUrl;

    public User(String nickname) {
        this.nickname = nickname;
        this.profileImageUrl = DEFAULT_PROFILE_IMAGE;
    }

    public static User createUser(String nickname) {
        return new User(nickname.substring(0, 8));
    }

}
