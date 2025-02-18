package org.learn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TestDomain2Test {

    @Test
    void test() {
        TestDomain2 testDomain2 = new TestDomain2();
        boolean check = testDomain2.check(3);
        assertThat(check).isFalse();
    }

    @Test
    void test2() {
        TestDomain2 testDomain2 = new TestDomain2();
        boolean check = testDomain2.check(2);
        assertThat(check).isTrue();
    }

}