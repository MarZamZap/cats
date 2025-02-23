package com.endava.cats.fuzzer.headers.only;

import com.endava.cats.http.ResponseCodeFamily;
import com.endava.cats.model.FuzzingStrategy;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class OnlySpacesInHeadersFuzzerTest {
    private OnlySpacesInHeadersFuzzer onlySpacesInHeadersFuzzer;

    @BeforeEach
    void setup() {
        onlySpacesInHeadersFuzzer = new OnlySpacesInHeadersFuzzer(null, null);
    }

    @Test
    void shouldProperlyOverrideMethods() {
        Assertions.assertThat(onlySpacesInHeadersFuzzer.description()).isNotNull();
        Assertions.assertThat(onlySpacesInHeadersFuzzer.typeOfDataSentToTheService()).isNotNull();
        Assertions.assertThat(onlySpacesInHeadersFuzzer.fuzzStrategy().get(0).name()).isEqualTo(FuzzingStrategy.replace().name());
        Assertions.assertThat(onlySpacesInHeadersFuzzer.fuzzStrategy().get(0).getData()).isEqualTo(" ");
        Assertions.assertThat(onlySpacesInHeadersFuzzer.getExpectedHttpCodeForRequiredHeadersFuzzed()).isEqualTo(ResponseCodeFamily.FOURXX);
        Assertions.assertThat(onlySpacesInHeadersFuzzer.getExpectedHttpForOptionalHeadersFuzzed()).isEqualTo(ResponseCodeFamily.TWOXX);
    }
}
