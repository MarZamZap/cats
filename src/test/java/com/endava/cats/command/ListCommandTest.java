package com.endava.cats.command;

import com.endava.cats.Fuzzer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import picocli.CommandLine;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@QuarkusTest
class ListCommandTest {

    @Inject
    Instance<Fuzzer> fuzzers;

    private ListCommand listCommand;

    @BeforeEach
    void setup() {
        listCommand = new ListCommand(fuzzers);
    }

    @Test
    void shouldListFuzzers() {
        ListCommand spyListCommand = Mockito.spy(listCommand);
        CommandLine commandLine = new CommandLine(spyListCommand);
        commandLine.execute("-f");
        Mockito.verify(spyListCommand, Mockito.times(1)).listFuzzers();
    }

    @Test
    void shouldNotListFuzzers() {
        ListCommand spyListCommand = Mockito.spy(listCommand);
        CommandLine commandLine = new CommandLine(spyListCommand);
        commandLine.execute("-f=false");
        Mockito.verify(spyListCommand, Mockito.times(0)).listFuzzers();
    }

    @Test
    void shouldListFuzzersStrategies() {
        ListCommand spyListCommand = Mockito.spy(listCommand);
        CommandLine commandLine = new CommandLine(spyListCommand);
        commandLine.execute("-s");
        Mockito.verify(spyListCommand, Mockito.times(1)).listFuzzerStrategies();
    }

    @Test
    void shouldNotListFuzzersStrategies() {
        ListCommand spyListCommand = Mockito.spy(listCommand);
        CommandLine commandLine = new CommandLine(spyListCommand);
        commandLine.execute("-s=false");
        Mockito.verify(spyListCommand, Mockito.times(0)).listFuzzerStrategies();
    }

    @ParameterizedTest
    @CsvSource({"-p,-c,src/test/resources/openapi.yml,1", "-p=false,-c,src/test/resources/openapi.yml,0", "-p,-c,openapi.yml,1"})
    void shouldListContractPaths(String arg1, String arg2, String arg3, int times) {
        ListCommand spyListCommand = Mockito.spy(listCommand);
        CommandLine commandLine = new CommandLine(spyListCommand);
        commandLine.execute(arg1, arg2, arg3);
        Mockito.verify(spyListCommand, Mockito.times(times)).listContractPaths();
    }

    @Test
    void shouldNotList() {
        ListCommand spyListCommand = Mockito.spy(listCommand);
        CommandLine commandLine = new CommandLine(spyListCommand);
        commandLine.execute();
        Mockito.verifyNoInteractions(spyListCommand);
    }
}
