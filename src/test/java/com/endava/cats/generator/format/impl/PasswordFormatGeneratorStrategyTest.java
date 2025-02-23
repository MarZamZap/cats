package com.endava.cats.generator.format.impl;

import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PasswordFormatGeneratorStrategyTest {
    @Test
    void givenAPasswordFormatGeneratorStrategy_whenGettingTheAlmostValidValue_thenTheValueIsReturnedAsExpected() {
        PasswordFormatGeneratorStrategy strategy = new PasswordFormatGeneratorStrategy();
        Assertions.assertThat(strategy.getAlmostValidValue()).isEqualTo("bgZD89DEkl");
    }

    @Test
    void givenAPasswordFormatGeneratorStrategy_whenGettingTheTotallyWrongValue_thenTheValueIsReturnedAsExpected() {
        PasswordFormatGeneratorStrategy strategy = new PasswordFormatGeneratorStrategy();
        Assertions.assertThat(strategy.getTotallyWrongValue()).isEqualTo("abcdefgh");
    }
}
