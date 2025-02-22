package com.endava.cats.fuzzer.fields;

import com.endava.cats.model.FuzzingData;
import io.quarkus.test.junit.QuarkusTest;
import io.swagger.v3.oas.models.media.NumberSchema;
import org.apache.commons.lang3.math.NumberUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class DecimalFieldsRightBoundaryFuzzerTest {

    private DecimalFieldsRightBoundaryFuzzer decimalFieldsRightBoundaryFuzzer;

    @BeforeEach
    void setup() {
        decimalFieldsRightBoundaryFuzzer = new DecimalFieldsRightBoundaryFuzzer(null, null, null, null);
    }

    @Test
    void givenANewDecimalFieldsFuzzer_whenCreatingANewInstance_thenTheMethodsBeingOverriddenAreMatchingTheDecimalFuzzer() {
        NumberSchema nrSchema = new NumberSchema();
        Assertions.assertThat(decimalFieldsRightBoundaryFuzzer.getSchemasThatTheFuzzerWillApplyTo().stream().anyMatch(schema -> schema.isAssignableFrom(NumberSchema.class))).isTrue();
        Assertions.assertThat(NumberUtils.isCreatable(decimalFieldsRightBoundaryFuzzer.getBoundaryValue(nrSchema))).isTrue();
        Assertions.assertThat(decimalFieldsRightBoundaryFuzzer.hasBoundaryDefined("test", FuzzingData.builder().build())).isTrue();
        Assertions.assertThat(decimalFieldsRightBoundaryFuzzer.description()).isNotNull();
    }
}