package com.endava.cats.fuzzer.fields;

import com.endava.cats.args.FilesArguments;
import com.endava.cats.annotations.FieldFuzzer;
import com.endava.cats.fuzzer.fields.base.BaseBoundaryFieldFuzzer;
import com.endava.cats.generator.simple.StringGenerator;
import com.endava.cats.io.ServiceCaller;
import com.endava.cats.model.FuzzingData;
import com.endava.cats.report.TestCaseListener;
import com.endava.cats.util.CatsUtil;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;

@Singleton
@FieldFuzzer
public class StringFieldsLeftBoundaryFuzzer extends BaseBoundaryFieldFuzzer {

    public StringFieldsLeftBoundaryFuzzer(ServiceCaller sc, TestCaseListener lr, CatsUtil cu, FilesArguments cp) {
        super(sc, lr, cu, cp);
    }

    @Override
    public List<Class<? extends Schema>> getSchemasThatTheFuzzerWillApplyTo() {
        return Collections.singletonList(StringSchema.class);
    }

    @Override
    public String getBoundaryValue(Schema schema) {
        return StringGenerator.generateLeftBoundString(schema);
    }

    @Override
    public boolean hasBoundaryDefined(String fuzzedField, FuzzingData data) {
        return data.getRequestPropertyTypes().get(fuzzedField).getMinLength() != null;
    }

    @Override
    public String description() {
        return "iterate through each String field and send requests with outside the range values on the left side in the targeted field";
    }
}
