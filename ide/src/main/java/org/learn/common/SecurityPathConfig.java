package org.learn.common;

import java.util.List;

public class SecurityPathConfig {

    public static final List<String> PUBLIC_URIS = List.of(
            "/favicon.ico",
            "/login.html",
            "/github.html",
            "/kakao.html",
            "/google.html",
            "/ide.html",
            "/userList.html"
    );

    public static final List<String> PUBLIC_START_URIS = List.of(
            "/h2-console"
    );

    public static final List<String> PUBLIC_END_URIS = List.of("/oauth-login");

    public static final List<String> ANONYMOUS_URIS = List.of(
            "/users/all",
            "/ide/run"
    );

}