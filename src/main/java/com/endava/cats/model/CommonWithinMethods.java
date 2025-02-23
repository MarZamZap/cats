package com.endava.cats.model;

import com.endava.cats.model.generator.PayloadGenerator;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class CommonWithinMethods {
    public static final String ZALGO_TEXT = "c̷̨̛̥̬͉̘̬̻̩͕͚̦̺̻͓̳͇̲̭̝̙̟̈́̉̐͂͒̆͂̿͌̑͐̌̇̈́̾̉̆̀̅̓͛͋̈̄͊̈̄̎̃̒͂̓̊̌̎̌̃́̅͊̏͘͘͘̕̕͘͠͝a̶͖̐͆͑́͆̓͗͆̏̑̈́̾͛̎̂̒̄̏̍͌͛̀́̄̓̍̐͂̀́̈́̂͐̕̕̕̚͘͠͝͠t̵̨̢̨͙̪̼͚͖̲̻̞̦̤̲̖͚̟̯͔̬̜̬͖̺͎̼̬̞̱̳͚͔͎̩̩̩̲̗̩̊̽̈́̔̀̍͒̓̂͐̾̆̐̒̄͂͒̽̾̔͊̒̀͗̿̈́͆͆̂͆̈́̋̏͊̉͌̒̏̓̑͛̉͘͜͜͜͝͝͠͠ş̶̨̢̧̛̛̱̜͈͓̗͍͈̰̱͔̥͙̺̤̠̩̮́̋̒͗̌̔̄̓̓͐̇̾̀́̓̆͗̂̐͊̓̓́̀͌̐̒̆̏̐͐̌̀́̈́̑̄͛̔̌͘̚̕͜͠ͅ ̸̡̡̧̡̨̧̧̯͚̥̙͉̲̠͚̼̤̹̹̳͕̙͔̺̥̼̙̙͚̳̰͕̤͕̀͒̈́̆̆̅̀̑̋̾͒̈́̅͌̀͑͋͋̎͂͂̄̑̆͒̃̓́̂̈́̑̄͝į̴̬͙͕̤͎͇̹̮̯̞̦̱̠̤̖̣̆͊̀̀̓͛͗͛̈͂̌̉̊͐̆̈̉͂͌̊́̉̋͘̚̚͜͝͝ș̷̡̛̛̮̲̥͙̞̤̘̉͛͗̿͂̏͛̾̂̂̄͗́̈́́̅̄̇̈́͗̀̂̈̉̐͑̏̒̈́͗̆͆̆̆͐̀͋̋͌̚̚͝͝ ̴̧̢̛̥̼̘̬̮͚͙̙̳͇̣̬̓̽̃̇̅͆͌̓̒̾͌̒͋͆́̓͛̔͛͒̉̔̏̔̂͐͛͗̾̎͂̏̋͘̚͝͝ç̵̡̧̛̛̟̩̲̲̲̫̺͎͎̘͎̘̱̭̬̗̎̾̏̂̏͑͊̾̎̂̉̊̉̐̓̾͒̓̓̒̔̽̄́͋̀̈́́̓̏͑͗̂̂̈́̒̚͘̕͘͝͠͝ͅͅͅơ̶̛̩̫̊̿̇͊͆́̅̈̽̆̓͛̌͐̍̀͒̐͑̀̎̀̀̉̑͛̔͋́̀͂̈̐̾̊̓͑̔͐̚̕͝͝͝͝͠ô̷̡̧̧̨̢̱͈̠̬̤̪̖̘͍̥̝͍̺̠̮̫̺̳͚͈͕̞̯̳̩̗̜̺̜̠͔̖̥͆͛͑́̆͛͐̓̒͊̊͑̽̄̐͊̓̃̚͜͜͝l";

    private CommonWithinMethods() {
        //ntd
    }

    public static List<FuzzingStrategy> getFuzzingStrategies(FuzzingData data, String fuzzedField, List<String> invisibleChars, boolean maintainSize) {
        Schema<?> fuzzedFieldSchema = data.getRequestPropertyTypes().get(fuzzedField);
        if (!StringSchema.class.isAssignableFrom(fuzzedFieldSchema.getClass())) {
            return Collections.singletonList(FuzzingStrategy.skip());
        }
        String initialValue = PayloadGenerator.generateValueBasedOnMinMAx(fuzzedFieldSchema);

        /*independent of the supplied strategy, we still maintain sizes for enums*/
        final boolean insertWithoutReplace = !maintainSize || !CollectionUtils.isEmpty(fuzzedFieldSchema.getEnum());

        return invisibleChars
                .stream().map(value -> FuzzingStrategy.replace().withData(insertInTheMiddle(initialValue, value, insertWithoutReplace)))
                .collect(Collectors.toList());
    }

    public static String insertInTheMiddle(String value, String whatToInsert, boolean insertWithoutReplace) {
        int position = value.length() / 2;
        return value.substring(0, position - (insertWithoutReplace ? 0 : 1)) + whatToInsert + value.substring(position + (insertWithoutReplace ? 0 : 1));
    }

    public static FuzzingStrategy getZalgoText(Schema<?> fuzzedFieldSchema) {
        int max = getMax(fuzzedFieldSchema);

        return FuzzingStrategy.replace().withData(ZALGO_TEXT.substring(0, max));
    }

    private static int getMax(Schema<?> fuzzedFieldSchema) {
        if (fuzzedFieldSchema.getMaxLength() == null) {
            return ZALGO_TEXT.length();
        }
        return fuzzedFieldSchema.getMaxLength() >= ZALGO_TEXT.length() ? ZALGO_TEXT.length() : fuzzedFieldSchema.getMaxLength();
    }
}
