package org.learn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TestDomainTest {

    @Test
    void test1() {
        TestDomain testDomain = new TestDomain();
        assertThat(testDomain.get()).isEqualTo(10);
    }

    @Test
    void test2() {
        TestDomain testDomain = new TestDomain();
        assertThat(testDomain.isEqualAndGreaterThan(10)).isTrue();
    }

    @Test
    void test3() {
        TestDomain testDomain = new TestDomain();
        assertThat(testDomain.isEqualAndGreaterThan(9)).isFalse();
    }

}