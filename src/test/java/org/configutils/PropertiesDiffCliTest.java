package org.configutils;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class PropertiesDiffCliTest {

    private final PrintStream console = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private static final String EXPECTED_USAGE_STRING = "usage: java -jar ConfigUtils-<version>-app.jar <file1> <file2> [OPTIONS]";

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @After
    public void tearDown() {
        System.setOut(console);
    }

    @Test
    public void shouldPrintHelpWhenNoArgs() {
        PropertiesDiffCli cli = buildPropertiesDiffCli(new String[]{});
        cli.execute();
        String output = outputStreamCaptor.toString().trim();

        assertThat(output, containsString(EXPECTED_USAGE_STRING + "1"));
    }

    @Test
    public void shouldPrintHelpWithHelpArg() {
        PropertiesDiffCli cli = buildPropertiesDiffCli("-h".split(" "));
        cli.execute();
        String output = outputStreamCaptor.toString().trim();

        assertThat(output, containsString(EXPECTED_USAGE_STRING));
    }

    @Test
    @Parameters({"file1", "file1,file2,file3"})
    public void shouldPrintHelpForInvalidArgumentCount(String[] args) {
        PropertiesDiffCli cli = buildPropertiesDiffCli(args);
        cli.execute();
        String output = outputStreamCaptor.toString().trim();

        assertThat(output, containsString(EXPECTED_USAGE_STRING));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionForBadInputFile() {
        PropertiesDiffCli cli = buildPropertiesDiffCli("___nofile1___ ___nofile2___".split(" "));
        cli.execute();
    }

    @Test
    public void shouldRunPropertiesDiffReportCommand_WhenNoCommandOptionsPresent() throws IOException {
        PropertiesDiffReportGeneratorCommand mock = mock(PropertiesDiffReportGeneratorCommand.class);

        CommandFactory mockCommandFactory = mock(CommandFactory.class);
        PropertiesDiffReportGeneratorCommand mockCommand = mock(PropertiesDiffReportGeneratorCommand.class);
        Mockito.when(mockCommandFactory.getPropertiesReportGeneratorCommand(any())).thenReturn(mockCommand);
        PropertiesDiffCli cli = new PropertiesDiffCli(mockCommandFactory, "file1 file2".split(" "));

        cli.execute();

        verify(mockCommand, times(1)).execute(new String[]{"file1", "file2"});
    }

    @Test
    public void shouldRunPropertiesDiffReportCommand_WhenReportOptionPresent() throws IOException {
        PropertiesDiffReportGeneratorCommand mock = mock(PropertiesDiffReportGeneratorCommand.class);

        CommandFactory mockCommandFactory = mock(CommandFactory.class);
        PropertiesDiffReportGeneratorCommand mockCommand = mock(PropertiesDiffReportGeneratorCommand.class);
        Mockito.when(mockCommandFactory.getPropertiesReportGeneratorCommand(any())).thenReturn(mockCommand);
        PropertiesDiffCli cli = new PropertiesDiffCli(mockCommandFactory, "file1 file2 -r".split(" "));

        cli.execute();

        verify(mockCommand, times(1)).execute(new String[]{"file1", "file2"});
    }

    @Test
    public void shouldRunPropertiesDiffReportCommand_WhenReportOptionPresentWithTextFormat() throws IOException {
        PropertiesDiffReportGeneratorCommand mock = mock(PropertiesDiffReportGeneratorCommand.class);

        CommandFactory mockCommandFactory = mock(CommandFactory.class);
        PropertiesDiffReportGeneratorCommand mockCommand = mock(PropertiesDiffReportGeneratorCommand.class);
        Mockito.when(mockCommandFactory.getPropertiesReportGeneratorCommand(any())).thenReturn(mockCommand);
        PropertiesDiffCli cli = new PropertiesDiffCli(mockCommandFactory, "file1 file2 -r text".split(" "));

        cli.execute();

        verify(mockCommandFactory, times(1)).getPropertiesReportGeneratorCommand("text");
        verify(mockCommand, times(1)).execute(new String[]{"file1", "file2"});
    }

    @Test
    public void shouldRunPropertiesDiffReportCommand_WhenReportOptionPresentWithHtmlFormat() throws IOException {
        PropertiesDiffReportGeneratorCommand mock = mock(PropertiesDiffReportGeneratorCommand.class);

        CommandFactory mockCommandFactory = mock(CommandFactory.class);
        PropertiesDiffReportGeneratorCommand mockCommand = mock(PropertiesDiffReportGeneratorCommand.class);
        Mockito.when(mockCommandFactory.getPropertiesReportGeneratorCommand(any())).thenReturn(mockCommand);
        PropertiesDiffCli cli = new PropertiesDiffCli(mockCommandFactory, "file1 file2 -r html".split(" "));

        cli.execute();

        verify(mockCommandFactory, times(1)).getPropertiesReportGeneratorCommand("html");
        verify(mockCommand, times(1)).execute(new String[]{"file1", "file2"});
    }

    @Test
    public void shouldRunPropertiesDiffToolCommand_WhenDiffToolOptionPresent() throws IOException {
        PropertiesDiffReportGeneratorCommand mock = mock(PropertiesDiffReportGeneratorCommand.class);

        CommandFactory mockCommandFactory = mock(CommandFactory.class);
        PropertiesDiffToolCommand mockCommand = mock(PropertiesDiffToolCommand.class);
        Mockito.when(mockCommandFactory.getPropertiesDiffToolCommand(any())).thenReturn(mockCommand);
        PropertiesDiffCli cli = new PropertiesDiffCli(mockCommandFactory, "file1 file2 -d meld".split(" "));

        cli.execute();

        verify(mockCommandFactory, times(1)).getPropertiesDiffToolCommand("meld");
        verify(mockCommand, times(1)).execute(new String[]{"file1", "file2"});
    }

    private PropertiesDiffCli buildPropertiesDiffCli(String[] args) {
        CommandFactory mockCommandFactory = mock(CommandFactory.class);
        return new PropertiesDiffCli(mockCommandFactory, args);
    }
}


