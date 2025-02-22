package com.endava.cats.fuzzer.fields;

import com.endava.cats.args.FilesArguments;
import com.endava.cats.io.ServiceCaller;
import com.endava.cats.model.FuzzingData;
import com.endava.cats.report.TestCaseListener;
import io.quarkus.test.junit.QuarkusTest;
import io.swagger.v3.oas.models.media.NumberSchema;
import org.apache.commons.lang3.math.NumberUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
class DecimalFieldsLeftBoundaryFuzzerTest {

    private ServiceCaller serviceCaller;
    private TestCaseListener testCaseListener;
    private FilesArguments filesArguments;

    private DecimalFieldsLeftBoundaryFuzzer decimalFieldsLeftBoundaryFuzzer;

    @BeforeEach
    void setup() {
        serviceCaller = Mockito.mock(ServiceCaller.class);
        testCaseListener = Mockito.mock(TestCaseListener.class);
        filesArguments = Mockito.mock(FilesArguments.class);
        decimalFieldsLeftBoundaryFuzzer = new DecimalFieldsLeftBoundaryFuzzer(serviceCaller, testCaseListener, null, filesArguments);
    }

    @Test
    void givenANewDecimalFieldsFuzzer_whenCreatingANewInstance_thenTheMethodsBeingOverriddenAreMatchingTheDecimalFuzzer() {
        NumberSchema nrSchema = new NumberSchema();
        Assertions.assertThat(decimalFieldsLeftBoundaryFuzzer.getSchemasThatTheFuzzerWillApplyTo().stream().anyMatch(schema -> schema.isAssignableFrom(NumberSchema.class))).isTrue();
        Assertions.assertThat(NumberUtils.isCreatable(decimalFieldsLeftBoundaryFuzzer.getBoundaryValue(nrSchema))).isTrue();
        Assertions.assertThat(decimalFieldsLeftBoundaryFuzzer.hasBoundaryDefined("test", FuzzingData.builder().build())).isTrue();
        Assertions.assertThat(decimalFieldsLeftBoundaryFuzzer.description()).isNotNull();
    }
}
