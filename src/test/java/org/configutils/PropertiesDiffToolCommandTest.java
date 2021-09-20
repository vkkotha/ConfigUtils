package org.configutils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PropertiesDiffToolCommandTest {

    @Mock
    Shell mockShell;
    @Captor
    ArgumentCaptor<String> shellSystemCommandArgCaptor;

    @Test
    public void shouldInvokeDiffExecutable() throws IOException {
        String diffExecutable = "meld";
        PropertiesDiffToolCommand command = new PropertiesDiffToolCommand(mockShell, diffExecutable);
        File testResources = new File("src/test/resources");
        final File file1 = new File(testResources, "test1.properties");
        final File file2 = new File(testResources, "test2.properties");

        command.execute(new String[] {file1.getAbsolutePath(), file2.getAbsolutePath()});
        verify(mockShell, times(1)).exec(contains(diffExecutable));
    }

    @Test
    public void shouldGenerateDiffPropertyFilesInTempFolder() throws IOException {
        String diffExecutable = "meld";
        PropertiesDiffToolCommand command = new PropertiesDiffToolCommand(mockShell, diffExecutable);
        File testResources = new File("src/test/resources");
        final File file1 = new File(testResources, "test1.properties");
        final File file2 = new File(testResources, "test2.properties");

        command.execute(new String[] {file1.getAbsolutePath(), file2.getAbsolutePath()});

        verify(mockShell).exec(shellSystemCommandArgCaptor.capture());
        String executedShellCommand = shellSystemCommandArgCaptor.getValue();
        String[] commandArray = executedShellCommand.split(" ");

        assertTrue(commandArray.length > 2);
        // Get last 2 arguments which are files
        String tempFile1 = commandArray[commandArray.length-2];
        String tempFile2 = commandArray[commandArray.length-1];

        assertTrue(new File(tempFile1).exists());
        assertTrue(new File(tempFile2).exists());
    }

    @Test
    public void shouldGeneratePropertyInOutput() throws IOException {

        StringWriter writer = new StringWriter();
        PropertiesDiffToolCommand command = new PropertiesDiffToolCommand(mockShell);

        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
                new PropertyValue("value1", true, 1),
                null);
        data.add(prop1);

        command.generateTempPropertiesFile(data, PropertiesDiffToolCommand.Source.S1, writer);
        String output = writer.toString();

        assertThat(output, containsString("key1=value1"));
    }

    @Test
    public void shouldGenerateCommentedPropertyInOutput() throws IOException {

        StringWriter writer = new StringWriter();
        PropertiesDiffToolCommand command = new PropertiesDiffToolCommand(mockShell);

        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
                new PropertyValue("value1", false, 1),
                null);
        data.add(prop1);

        command.generateTempPropertiesFile(data, PropertiesDiffToolCommand.Source.S1, writer);
        String output = writer.toString();

        assertThat(output, containsString("# key1=value1"));
    }

    @Test
    public void shouldOnlyGeneratePropertiesInSource1() throws IOException {

        StringWriter writer = new StringWriter();
        PropertiesDiffToolCommand command = new PropertiesDiffToolCommand(mockShell);

        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
                new PropertyValue("value1", true, 1),
                new PropertyValue("value2", true, 1));
        data.add(prop1);

        command.generateTempPropertiesFile(data, PropertiesDiffToolCommand.Source.S1, writer);
        String output = writer.toString();

        assertThat(output, containsString("key1=value1"));
        assertThat(output, not(containsString("key1=value2")));
    }

    @Test
    public void shouldOnlyGeneratePropertiesInSource2() throws IOException {

        StringWriter writer = new StringWriter();
        PropertiesDiffToolCommand command = new PropertiesDiffToolCommand(mockShell);

        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
                new PropertyValue("value1", false, 1),
                new PropertyValue("value2", false, 1));
        data.add(prop1);

        command.generateTempPropertiesFile(data, PropertiesDiffToolCommand.Source.S2, writer);
        String output = writer.toString();

        assertThat(output, not(containsString("# key1=value1")));
        assertThat(output, containsString("# key1=value2"));
    }

    @Test
    public void shouldAddLineForMissingPropertyInS1() throws IOException {

        StringWriter writer = new StringWriter();
        PropertiesDiffToolCommand command = new PropertiesDiffToolCommand(mockShell);

        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
                null,
                new PropertyValue("value1", true, 1));
        data.add(prop1);

        command.generateTempPropertiesFile(data, PropertiesDiffToolCommand.Source.S2, writer);
        String output = writer.toString();

        assertThat(output, not(containsString("- key1=")));
    }

    @Test
    public void shouldAddLineForMissingAndCommentedPropertyInS2() throws IOException {

        StringWriter writer = new StringWriter();
        PropertiesDiffToolCommand command = new PropertiesDiffToolCommand(mockShell);

        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
                new PropertyValue("value1", false, 1),
                null);
        data.add(prop1);

        command.generateTempPropertiesFile(data, PropertiesDiffToolCommand.Source.S2, writer);
        String output = writer.toString();

        assertThat(output, not(containsString("- # key1=")));
    }
}
