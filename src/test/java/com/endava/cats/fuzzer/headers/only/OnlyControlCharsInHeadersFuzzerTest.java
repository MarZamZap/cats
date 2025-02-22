package com.endava.cats.fuzzer.headers.only;

import com.endava.cats.model.FuzzingStrategy;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class OnlyControlCharsInHeadersFuzzerTest {
    private OnlyControlCharsInHeadersFuzzer onlyControlCharsInHeadersFuzzer;

    @BeforeEach
    void setup() {
        onlyControlCharsInHeadersFuzzer = new OnlyControlCharsInHeadersFuzzer(null, null);
    }

    @Test
    void givenANewSpacesOnlyInHeadersFuzzer_whenCreatingANewInstance_thenTheMethodsBeingOverriddenAreMatchingTheSpacesOnlyInHeadersFuzzer() {
        Assertions.assertThat(onlyControlCharsInHeadersFuzzer.description()).isNotNull();
        Assertions.assertThat(onlyControlCharsInHeadersFuzzer.typeOfDataSentToTheService()).isNotNull();
        Assertions.assertThat(onlyControlCharsInHeadersFuzzer.fuzzStrategy().get(0).name()).isEqualTo(FuzzingStrategy.replace().name());
        Assertions.assertThat(onlyControlCharsInHeadersFuzzer.fuzzStrategy().get(1).getData()).isEqualTo("\u0000");
        Assertions.assertThat(onlyControlCharsInHeadersFuzzer.matchResponseSchema()).isFalse();
    }
}
