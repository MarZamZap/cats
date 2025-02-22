package com.endava.cats.fuzzer.contract;

import com.endava.cats.Fuzzer;
import com.endava.cats.model.CatsRequest;
import com.endava.cats.model.CatsResponse;
import com.endava.cats.model.FuzzingData;
import com.endava.cats.report.TestCaseListener;
import io.github.ludovicianul.prettylogger.PrettyLogger;
import io.github.ludovicianul.prettylogger.PrettyLoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Base class for all Contract Fuzzers. If you need additional behaviour please make sure you don't break existing Fuzzers.
 * Contract Fuzzers are only focused on contract following best practices without calling the actual service.
 */
public abstract class BaseContractInfoFuzzer implements Fuzzer {
    protected static final String DESCRIPTION = "description";
    protected static final String COMMA = ", ";
    protected static final String IS_EMPTY = " is empty";
    protected static final String IS_TOO_SHORT = " is too short";
    protected static final String EMPTY = "";
    protected final TestCaseListener testCaseListener;
    protected final List<String> fuzzedPaths = new ArrayList<>();
    private final PrettyLogger log = PrettyLoggerFactory.getLogger(this.getClass());

    protected BaseContractInfoFuzzer(TestCaseListener tcl) {
        this.testCaseListener = tcl;
    }

    @Override
    public void fuzz(FuzzingData data) {
        if (!fuzzedPaths.contains(this.runKey(data))) {
            testCaseListener.createAndExecuteTest(log, this, () -> addDefaultsAndProcess(data));

            fuzzedPaths.add(this.runKey(data));
        }
    }

    /**
     * Contract Fuzzers are only analyzing the contract without doing any HTTP Call.
     * This is why we set the below default values for all Contract Fuzzers.
     *
     * @param data the current FuzzingData
     */
    private void addDefaultsAndProcess(FuzzingData data) {
        testCaseListener.addPath(data.getPath());
        testCaseListener.addFullRequestPath("NA");
        testCaseListener.addRequest(CatsRequest.empty());
        testCaseListener.addResponse(CatsResponse.empty());

        this.process(data);
    }

    protected <T> String getOrEmpty(Supplier<T> function, String toReturn) {
        if (function.get() == null) {
            return toReturn;
        }
        return EMPTY;
    }

    /**
     * HTML bold the text.
     *
     * @param text the text
     * @return the text enclosed in <strong></strong>
     */
    protected String bold(String text) {
        return "<strong>" + text + "</strong>";
    }

    /**
     * Adds a new HTML line break.
     *
     * @param times how many line breaks
     * @return line breaks according to the given times
     */
    protected String newLine(int times) {
        return StringUtils.repeat("<br />", times);
    }


    protected String trailNewLines(String text, int newLines) {
        return text + newLine(newLines);
    }

    /**
     * Each Fuzzer will implement this in order to provide specific logic.
     *
     * @param data the current FuzzingData object
     */
    public abstract void process(FuzzingData data);

    /**
     * This will avoid running the same fuzzer more than once if not relevant. You can define the runKey based on any combination of elements
     * that will make the run unique for the context. Some Fuzzers might run once per contract while others once per path.
     *
     * @param data the FuzzingData
     * @return a unique running key for the Fuzzer context
     */
    protected abstract String runKey(FuzzingData data);

    @Override
    public String toString() {
        return this.getClass().getSimpleName().replace("_Subclass", "");
    }
}
