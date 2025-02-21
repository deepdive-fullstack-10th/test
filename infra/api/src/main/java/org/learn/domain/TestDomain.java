package org.learn.domain;

public class TestDomain {

    private static final int NUMBER = 10;

    public int get() {
        return NUMBER;
    }

    public boolean isEqualAndGreaterThan(int number) {
        return number >= NUMBER;
    }

}
