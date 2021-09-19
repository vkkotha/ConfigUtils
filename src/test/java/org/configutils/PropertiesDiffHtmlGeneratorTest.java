package org.configutils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

public class PropertiesDiffHtmlGeneratorTest {
    private final PrintStream console = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @After
    public void tearDown() {
        System.setOut(console);
    }

    @Test
    public void shouldRenderMarkupFromHtmlTemplate() throws IOException {
        PropertiesDiffHtmlGenerator diffGenerator = new PropertiesDiffHtmlGenerator();

        String markup = diffGenerator.generateMarkup(null);

        assertNotNull(markup);
    }

    @Test
    public void shouldHavePropertyInfoInRenderedMarkup() throws IOException {
        PropertiesDiffHtmlGenerator diffGenerator = new PropertiesDiffHtmlGenerator();
        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
                new PropertyValue("value1", true, 1),
                null);
        data.add(prop1);

        String markup = diffGenerator.generateMarkup(data);

        assertNotNull(markup);
        assertThat(markup, allOf(containsString("key1"), containsString("value1")));
        assertThat(markup, allOf(containsString("Source1"), containsString("Source2")));
    }

    @Test
    public void shouldGenerateMarkupFromFiles() throws IOException {
        File testResources = new File("src/test/resources");
        final File file1 = new File(testResources, "test1.properties");
        final File file2 = new File(testResources, "test2.properties");

        PropertiesDiffHtmlGenerator diffGenerator = new PropertiesDiffHtmlGenerator(file1.getAbsolutePath(), file2.getAbsolutePath());

        String markup = diffGenerator.generateMarkup();

        assertNotNull(markup);
        assertThat(markup, allOf(containsString("key1"), containsString("value1")));
        assertThat(markup, allOf(containsString("test1.properties"), containsString("test2.properties")));
    }

    @Test
    public void shouldWriteReportToConsole() throws IOException {
        File testResources = new File("src/test/resources");
        final File file1 = new File(testResources, "test1.properties");
        final File file2 = new File(testResources, "test2.properties");

        PropertiesDiffHtmlGenerator diffGenerator = new PropertiesDiffHtmlGenerator(file1.getAbsolutePath(), file2.getAbsolutePath());

        diffGenerator.generateReport();
        String markup = outputStreamCaptor.toString().trim();

        assertThat(markup, allOf(containsString("key1"), containsString("value1")));
        assertThat(markup, allOf(containsString("test1.properties"), containsString("test2.properties")));
    }
}
