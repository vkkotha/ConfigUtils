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

import static org.configutils.PropertiesDiffTextGenerator.DiffCategory;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

public class PropertiesDiffTextGeneratorTest {
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
    public void shouldGenerateDiffText() throws IOException {
        PropertiesDiffTextGenerator diffGenerator = new PropertiesDiffTextGenerator();

        String result = diffGenerator.generateText(null);

        assertNotNull(result);
    }

    @Test
    public void shouldHaveMatchingPropertyInfoInText() throws IOException {
        PropertiesDiffTextGenerator diffGenerator = new PropertiesDiffTextGenerator();
        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
            new PropertyValue("value1", true, 1),
            new PropertyValue("value1", true, 1));
        data.add(prop1);

        String text = diffGenerator.generateText(data);

        assertThat(text, containsString(DiffCategory.MATCHING.getValue()));
        assertThat(text, not(containsString(DiffCategory.MISMATCH.getValue())));
        assertThat(text, not(containsString(DiffCategory.S1_ONLY.getValue())));
        assertThat(text, not(containsString(DiffCategory.S2_ONLY.getValue())));
        assertThat(text, containsString("key1=value1"));
    }

    @Test
    public void shouldHaveMisMatchedPropertyInfoInText() throws IOException {
        PropertiesDiffTextGenerator diffGenerator = new PropertiesDiffTextGenerator();
        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
            new PropertyValue("value1", true, 1),
            new PropertyValue("value2", true, 2));
        data.add(prop1);

        String text = diffGenerator.generateText(data);

        assertThat(text, not(containsString(DiffCategory.MATCHING.getValue())));
        assertThat(text, containsString(DiffCategory.MISMATCH.getValue()));
        assertThat(text, not(containsString(DiffCategory.S1_ONLY.getValue())));
        assertThat(text, not(containsString(DiffCategory.S2_ONLY.getValue())));

        assertThat(text, containsString("> key1=value1"));
        assertThat(text, containsString("< key1=value2"));
    }

    @Test
    public void shouldHaveSource1OnlyPropertyInfoInText() throws IOException {
        PropertiesDiffTextGenerator diffGenerator = new PropertiesDiffTextGenerator();
        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
            new PropertyValue("value1", true, 1),
            null);
        data.add(prop1);

        String text = diffGenerator.generateText(data);

        assertThat(text, not(containsString(DiffCategory.MATCHING.getValue())));
        assertThat(text, not(containsString(DiffCategory.MISMATCH.getValue())));
        assertThat(text, containsString(DiffCategory.S1_ONLY.getValue()));
        assertThat(text, not(containsString(DiffCategory.S2_ONLY.getValue())));

        assertThat(text, containsString("key1=value1"));
    }

    @Test
    public void shouldHaveSource2OnlyPropertyInfoInText() throws IOException {
        PropertiesDiffTextGenerator diffGenerator = new PropertiesDiffTextGenerator();
        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
            null,
            new PropertyValue("value1", true, 1));
        data.add(prop1);

        String text = diffGenerator.generateText(data);

        assertThat(text, not(containsString(DiffCategory.MATCHING.getValue())));
        assertThat(text, not(containsString(DiffCategory.MISMATCH.getValue())));
        assertThat(text, not(containsString(DiffCategory.S1_ONLY.getValue())));
        assertThat(text, containsString(DiffCategory.S2_ONLY.getValue()));

        assertThat(text, containsString("key1=value1"));
    }

    @Test
    public void shouldHaveHashBeforeDisabledProperty() {
        PropertiesDiffTextGenerator diffGenerator = new PropertiesDiffTextGenerator();
        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
            null,
            new PropertyValue("value1", false, 1));
        data.add(prop1);

        String text = diffGenerator.generateText(data);

        assertThat(text, containsString("# key1=value1"));
    }

    @Test
    public void shouldHaveHashAndShouldBeInMatchingSection_If_DisabledInBothSources() {
        PropertiesDiffTextGenerator diffGenerator = new PropertiesDiffTextGenerator();
        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
            new PropertyValue("value1", false, 1),
            new PropertyValue("value1", false, 1));
        data.add(prop1);

        String text = diffGenerator.generateText(data);

        assertThat(text, containsString(DiffCategory.MATCHING.getValue()));
        assertThat(text, not(containsString(DiffCategory.MISMATCH.getValue())));
        assertThat(text, not(containsString(DiffCategory.S1_ONLY.getValue())));
        assertThat(text, not(containsString(DiffCategory.S2_ONLY.getValue())));

        assertThat(text, containsString("# key1=value1"));
    }

    @Test
    public void shouldHaveHashAndShouldBeInMismatchSection_If_EnabledOnlyInOne() {
        PropertiesDiffTextGenerator diffGenerator = new PropertiesDiffTextGenerator();
        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
            new PropertyValue("value1", true, 1),
            new PropertyValue("value1", false, 1));
        data.add(prop1);

        String text = diffGenerator.generateText(data);

        assertThat(text, not(containsString(DiffCategory.MATCHING.getValue())));
        assertThat(text, containsString(DiffCategory.MISMATCH.getValue()));
        assertThat(text, not(containsString(DiffCategory.S1_ONLY.getValue())));
        assertThat(text, not(containsString(DiffCategory.S2_ONLY.getValue())));

        assertThat(text, containsString("# key1=value1"));
    }

    @Test
    public void shouldHaveHashAndShouldBeInS1Section_If_DisabledPropertyOnlyInS1() {
        PropertiesDiffTextGenerator diffGenerator = new PropertiesDiffTextGenerator();
        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
            new PropertyValue("value1", false, 1),
            null);
        data.add(prop1);

        String text = diffGenerator.generateText(data);

        assertThat(text, not(containsString(DiffCategory.MATCHING.getValue())));
        assertThat(text, not(containsString(DiffCategory.MISMATCH.getValue())));
        assertThat(text, containsString(DiffCategory.S1_ONLY.getValue()));
        assertThat(text, not(containsString(DiffCategory.S2_ONLY.getValue())));

        assertThat(text, containsString("# key1=value1"));
    }

    @Test
    public void shouldHaveHashAndShouldBeInS2Section_If_DisabledPropertyOnlyInS2() {
        PropertiesDiffTextGenerator diffGenerator = new PropertiesDiffTextGenerator();
        List<PropertyDiffDetail> data = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1",
            null,
            new PropertyValue("value1", false, 1));
        data.add(prop1);

        String text = diffGenerator.generateText(data);

        assertThat(text, not(containsString(DiffCategory.MATCHING.getValue())));
        assertThat(text, not(containsString(DiffCategory.MISMATCH.getValue())));
        assertThat(text, not(containsString(DiffCategory.S1_ONLY.getValue())));
        assertThat(text, containsString(DiffCategory.S2_ONLY.getValue()));

        assertThat(text, containsString("# key1=value1"));
    }

    @Test
    public void shouldGenerateDiffOutputFromFiles() throws IOException {
        File testResources = new File("src/test/resources");
        final File file1 = new File(testResources, "test1.properties");
        final File file2 = new File(testResources, "test2.properties");

        PropertiesDiffTextGenerator diffGenerator = new PropertiesDiffTextGenerator(file1.getAbsolutePath(), file2.getAbsolutePath());

        String text = diffGenerator.generateText();

        assertNotNull(text);
        assertThat(text, containsString(DiffCategory.MATCHING.getValue()));
        assertThat(text, containsString("key1=value1"));
    }

    @Test
    public void shouldWriteReportToConsole() throws IOException {
        File testResources = new File("src/test/resources");
        final File file1 = new File(testResources, "test1.properties");
        final File file2 = new File(testResources, "test2.properties");

        PropertiesDiffTextGenerator diffGenerator = new PropertiesDiffTextGenerator(file1.getAbsolutePath(), file2.getAbsolutePath());

        diffGenerator.generateReport();
        String text = outputStreamCaptor.toString().trim();

        assertThat(text, containsString(DiffCategory.MATCHING.getValue()));
        assertThat(text, containsString("key1=value1"));
    }

}
