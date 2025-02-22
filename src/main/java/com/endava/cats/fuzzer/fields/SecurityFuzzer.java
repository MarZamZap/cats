package com.endava.cats.fuzzer.fields;

import com.endava.cats.annotations.SpecialFuzzer;
import com.endava.cats.args.FilesArguments;
import com.endava.cats.dsl.CatsDSLWords;
import com.endava.cats.fuzzer.CustomFuzzerUtil;
import com.endava.cats.fuzzer.fields.base.CustomFuzzerBase;
import com.endava.cats.model.CatsField;
import com.endava.cats.model.FuzzingData;
import com.endava.cats.model.util.JsonUtils;
import io.github.ludovicianul.prettylogger.PrettyLogger;
import io.github.ludovicianul.prettylogger.PrettyLoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.inject.Singleton;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
@SpecialFuzzer
public class SecurityFuzzer implements CustomFuzzerBase {
    private final PrettyLogger log = PrettyLoggerFactory.getLogger(this.getClass());

    private final FilesArguments filesArguments;
    private final CustomFuzzerUtil customFuzzerUtil;

    public SecurityFuzzer(FilesArguments cp, CustomFuzzerUtil cfu) {
        this.filesArguments = cp;
        this.customFuzzerUtil = cfu;
    }

    @Override
    public void fuzz(FuzzingData data) {
        if (!filesArguments.getSecurityFuzzerDetails().isEmpty()) {
            this.processSecurityFuzzerFile(data);
        }
    }

    protected void processSecurityFuzzerFile(FuzzingData data) {
        Map<String, Object> currentPathValues = this.getCurrentPathValues(data);
        if (currentPathValues != null) {
            currentPathValues.forEach((key, value) -> this.executeTestCases(data, key, value));
        } else {
            log.skip("Skipping path [{}] as it was not configured in securityFuzzerFile", data.getPath());
        }
    }

    private Map<String, Object> getCurrentPathValues(FuzzingData data) {
        Map<String, Object> currentPathValues = filesArguments.getSecurityFuzzerDetails().get(data.getPath());
        if (CollectionUtils.isEmpty(currentPathValues)) {
            currentPathValues = filesArguments.getSecurityFuzzerDetails().get(CatsDSLWords.ALL);
        }

        return currentPathValues;
    }

    private void executeTestCases(FuzzingData data, String key, Object value) {
        log.info("Path [{}] has the following security configuration [{}]", data.getPath(), value);
        Map<String, Object> individualTestConfig = (Map<String, Object>) value;
        String stringsFile = String.valueOf(individualTestConfig.get(CatsDSLWords.STRINGS_FILE));

        try {
            List<String> nastyStrings = Files.readAllLines(Paths.get(stringsFile));
            log.start("Parsing stringsFile...");
            log.complete("stringsFile parsed successfully! Found {} entries", nastyStrings.size());
            List<String> targetFields = this.getTargetFields(individualTestConfig, data);

            for (String targetField : targetFields) {
                log.info("Fuzzing field [{}]", targetField);
                Map<String, Object> individualTestConfigClone = individualTestConfig.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                individualTestConfigClone.put(targetField, nastyStrings);
                individualTestConfigClone.put(CatsDSLWords.DESCRIPTION, individualTestConfig.get(CatsDSLWords.DESCRIPTION) + ", field [" + targetField + "]");
                individualTestConfigClone.remove(CatsDSLWords.TARGET_FIELDS);
                individualTestConfigClone.remove(CatsDSLWords.TARGET_FIELDS_TYPES);
                individualTestConfigClone.remove(CatsDSLWords.STRINGS_FILE);
                customFuzzerUtil.executeTestCases(data, key, individualTestConfigClone, this);
            }

        } catch (Exception e) {
            log.error("Invalid stringsFile [{}]", stringsFile, e);
        }
    }

    private List<String> getTargetFields(Map<String, Object> individualTestConfig, FuzzingData data) {
        String[] targetFields = this.extractListEntry(individualTestConfig, CatsDSLWords.TARGET_FIELDS);
        String[] targetFieldsTypes = this.extractListEntry(individualTestConfig, CatsDSLWords.TARGET_FIELDS_TYPES);

        List<String> catsFields = data.getAllFieldsAsCatsFields().stream()
                .filter(catsField -> Arrays.asList(targetFieldsTypes).contains(StringUtils.toRootLowerCase(catsField.getSchema().getType()))
                        || Arrays.asList(targetFieldsTypes).contains(StringUtils.toRootLowerCase(catsField.getSchema().getFormat())))
                .map(CatsField::getName)
                .filter(name -> !JsonUtils.getVariableFromJson(data.getPayload(), name).equals(JsonUtils.NOT_SET))
                .collect(Collectors.toList());

        catsFields.addAll(Arrays.asList(targetFields));
        catsFields.remove("");

        return catsFields;
    }

    private String[] extractListEntry(Map<String, Object> individualTestConfig, String key) {
        return String.valueOf(individualTestConfig.getOrDefault(key, "")).replace("[", "")
                .replace(" ", "").replace("]", "").split(",");
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName().replace("_Subclass", "");
    }

    @Override
    public String description() {
        return "use custom dictionaries of 'nasty' strings to target specific fields or data types";
    }

    @Override
    public List<String> reservedWords() {
        return Arrays.asList(CatsDSLWords.EXPECTED_RESPONSE_CODE, CatsDSLWords.DESCRIPTION, CatsDSLWords.OUTPUT, CatsDSLWords.VERIFY, CatsDSLWords.STRINGS_FILE, CatsDSLWords.TARGET_FIELDS,
                CatsDSLWords.TARGET_FIELDS_TYPES, CatsDSLWords.MAP_VALUES, CatsDSLWords.ONE_OF_SELECTION, CatsDSLWords.ADDITIONAL_PROPERTIES, CatsDSLWords.ELEMENT, CatsDSLWords.HTTP_METHOD);
    }
}
