package com.endava.cats.fuzzer.headers.base;

import com.endava.cats.http.ResponseCodeFamily;
import com.endava.cats.io.ServiceCaller;
import com.endava.cats.model.FuzzingStrategy;
import com.endava.cats.report.TestCaseListener;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
class Expect2XXBaseHeadersFuzzerTest {
    private Expect2XXBaseHeadersFuzzer expect2XXBaseHeadersFuzzer;

    @BeforeEach
    void setup() {
        expect2XXBaseHeadersFuzzer = new My2XXFuzzer(null, null);
    }

    @Test
    void givenANewExpect2XXBaseHeadersFuzzer_whenCreatingANewInstance_thenTheMethodsBeingOverriddenAreMatchingTheExpect2XXBaseHeadersFuzzer() {
        Assertions.assertThat(expect2XXBaseHeadersFuzzer.getExpectedHttpCodeForRequiredHeadersFuzzed()).isEqualTo(ResponseCodeFamily.TWOXX);
        Assertions.assertThat(expect2XXBaseHeadersFuzzer.getExpectedHttpForOptionalHeadersFuzzed()).isEqualTo(ResponseCodeFamily.TWOXX);
        Assertions.assertThat(expect2XXBaseHeadersFuzzer).hasToString(expect2XXBaseHeadersFuzzer.getClass().getSimpleName());
    }

    static class My2XXFuzzer extends Expect2XXBaseHeadersFuzzer {

        public My2XXFuzzer(ServiceCaller sc, TestCaseListener lr) {
            super(sc, lr);
        }

        @Override
        protected String typeOfDataSentToTheService() {
            return null;
        }

        @Override
        protected List<FuzzingStrategy> fuzzStrategy() {
            return null;
        }

        @Override
        public String description() {
            return null;
        }
    }
}
