package com.endava.cats.fuzzer.fields;

import com.endava.cats.annotations.FieldFuzzer;
import com.endava.cats.annotations.ValidateAndSanitize;
import com.endava.cats.args.FilesArguments;
import com.endava.cats.fuzzer.fields.base.InvisibleCharsBaseTrimValidateFuzzer;
import com.endava.cats.http.ResponseCodeFamily;
import com.endava.cats.io.ServiceCaller;
import com.endava.cats.model.CommonWithinMethods;
import com.endava.cats.model.FuzzingData;
import com.endava.cats.model.FuzzingStrategy;
import com.endava.cats.report.TestCaseListener;
import com.endava.cats.util.CatsUtil;
import io.swagger.v3.oas.models.media.Schema;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;

@Singleton
@FieldFuzzer
@ValidateAndSanitize
public class ZalgoTextInStringFieldsValidateSanitizeFuzzer extends InvisibleCharsBaseTrimValidateFuzzer {
    protected ZalgoTextInStringFieldsValidateSanitizeFuzzer(ServiceCaller sc, TestCaseListener lr, CatsUtil cu, FilesArguments cp) {
        super(sc, lr, cu, cp);
    }

    @Override
    public ResponseCodeFamily getExpectedHttpCodeWhenFuzzedValueNotMatchesPattern() {
        return ResponseCodeFamily.FOURXX;
    }

    @Override
    public ResponseCodeFamily getExpectedHttpCodeWhenRequiredFieldsAreFuzzed() {
        return ResponseCodeFamily.FOURXX;
    }

    @Override
    public ResponseCodeFamily getExpectedHttpCodeWhenOptionalFieldsAreFuzzed() {
        return ResponseCodeFamily.FOURXX;
    }

    @Override
    public List<FuzzingStrategy> getFieldFuzzingStrategy(FuzzingData data, String fuzzedField) {
        Schema<?> fuzzedFieldSchema = data.getRequestPropertyTypes().get(fuzzedField);
        return List.of(CommonWithinMethods.getZalgoText(fuzzedFieldSchema));
    }

    @Override
    protected String typeOfDataSentToTheService() {
        return "values containing zalgo text";
    }

    @Override
    public List<String> getInvisibleChars() {
        return Collections.emptyList();
    }

    @Override
    public FuzzingStrategy concreteFuzzStrategy() {
        return FuzzingStrategy.replace();
    }
}