package org.configutils;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PropertiesDiffGeneratorTest {

    private List<PropertyDiffDetail> generateDiff(String props1, String props2) throws IOException {
        Reader reader1 = new StringReader(props1);
        Reader reader2 = new StringReader(props2);
        PropertiesDiffGenerator diffGenerator = createPropertiesDiffGenerator(reader1, reader2);
        List<PropertyDiffDetail> result = diffGenerator.generateDiff(reader1, reader2);
        return result;
    }

    private PropertiesDiffGenerator createPropertiesDiffGenerator(Reader reader1, Reader reader2) {
        return new PropertiesDiffGenerator() {
            @Override
            public void generateReport() throws IOException {
            }
        };
    }

    @Test
    public void shouldGenerateDiffMap() throws IOException {
        String props1 = "key1=value1";
        String props2 = "key1=value2";

        List<PropertyDiffDetail> expected = new ArrayList<>();
        PropertyDiffDetail prop1 = new PropertyDiffDetail("key1", new PropertyValue("value1", true, 1),
            new PropertyValue("value2", true, 1));
        expected.add(prop1);

        List<PropertyDiffDetail> result = generateDiff(props1, props2);

        assertNotNull(result);
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testForMatchingEntries() throws IOException {
        String props1 = "key1=value1";
        String props2 = "key1=value1";

        List<PropertyDiffDetail> result = generateDiff(props1, props2);
        assertNotNull(result);
        PropertyDiffDetail item = result.get(0);
        assertEquals("key1", item.getProperty());
        assertEquals(Integer.valueOf(1), item.getS1_line_number());
        assertEquals("value1", item.getS1_value());
        assertEquals(false, item.getS1_disabled());
        assertEquals(Integer.valueOf(1), item.getS2_line_number());
        assertEquals("value1", item.getS2_value());
        assertEquals(false, item.getS2_disabled());

    }

    @Test
    public void testForMismatchedEntries() throws IOException {
        String props1 = "key1=value1";
        String props2 = "key1=value2";

        List<PropertyDiffDetail> result = generateDiff(props1, props2);
        assertNotNull(result);
        PropertyDiffDetail item = result.get(0);
        assertEquals("key1", item.getProperty());
        assertEquals(Integer.valueOf(1), item.getS1_line_number());
        assertEquals("value1", item.getS1_value());
        assertEquals(false, item.getS1_disabled());
        assertEquals(Integer.valueOf(1), item.getS2_line_number());
        assertEquals("value2", item.getS2_value());
        assertEquals(false, item.getS2_disabled());
    }

    @Test
    public void testForMissingInSource1() throws IOException {
        String props1 = "key1=value1";
        String props2 = "key1=value2\n"
            + "key2=value2";

        List<PropertyDiffDetail> result = generateDiff(props1, props2);
        assertNotNull(result);
        assertEquals(2, result.size());

        PropertyDiffDetail item1 = result.get(0);
        assertEquals("key1", item1.getProperty());
        assertEquals(Integer.valueOf(1), item1.getS1_line_number());
        assertEquals("value1", item1.getS1_value());
        assertEquals(false, item1.getS1_disabled());
        assertEquals(Integer.valueOf(1), item1.getS2_line_number());
        assertEquals("value2", item1.getS2_value());
        assertEquals(false, item1.getS2_disabled());

        PropertyDiffDetail item2 = result.get(1);
        assertEquals("key2", item2.getProperty());
        assertNull(item2.getS1_line_number());
        assertNull(item2.getS1_value());
        assertNull(item2.getS1_disabled());
        assertEquals(Integer.valueOf(2), item2.getS2_line_number());
        assertEquals("value2", item2.getS2_value());
        assertEquals(false, item2.getS2_disabled());
    }

    @Test
    public void testForMissingInSource2() throws IOException {
        String props1 = "key1=value1\n"
            + "key2=value2";
        String props2 = "key2=value3";

        List<PropertyDiffDetail> result = generateDiff(props1, props2);
        assertNotNull(result);
        assertEquals(2, result.size());

        PropertyDiffDetail item1 = result.get(0);
        assertEquals("key1", item1.getProperty());
        assertEquals(Integer.valueOf(1), item1.getS1_line_number());
        assertEquals("value1", item1.getS1_value());
        assertEquals(false, item1.getS1_disabled());
        assertNull(item1.getS2_line_number());
        assertNull(item1.getS2_value());
        assertNull(item1.getS2_disabled());

        PropertyDiffDetail item2 = result.get(1);
        assertEquals("key2", item2.getProperty());
        assertEquals(Integer.valueOf(2), item2.getS1_line_number());
        assertEquals("value2", item2.getS1_value());
        assertEquals(false, item2.getS1_disabled());
        assertEquals(Integer.valueOf(1), item2.getS2_line_number());
        assertEquals("value3", item2.getS2_value());
        assertEquals(false, item2.getS2_disabled());
    }

    @Test
    public void testForMultilineMatch() throws IOException {
        String props1 = "key1=match \\\n"
            + "  test\n";
        String props2 = "key1=match \\\n"
            + "  test\n";

        List<PropertyDiffDetail> result = generateDiff(props1, props2);

        assertNotNull(result);
        assertEquals(1, result.size());

        PropertyDiffDetail item1 = result.get(0);
        assertEquals("key1", item1.getProperty());
        assertEquals(Integer.valueOf(1), item1.getS1_line_number());
        assertEquals("match \n  test", item1.getS1_value());
        assertEquals(false, item1.getS1_disabled());
        assertEquals(Integer.valueOf(1), item1.getS2_line_number());
        assertEquals("match \n  test", item1.getS2_value());
        assertEquals(false, item1.getS2_disabled());
    }

    @Test
    public void testForMultilineMisMatch() throws IOException {
        String props1 = "key1=match \\\n"
            + "  test\n";
        String props2 = "key1=mismatch";

        List<PropertyDiffDetail> result = generateDiff(props1, props2);

        assertNotNull(result);
        assertEquals(1, result.size());

        PropertyDiffDetail item1 = result.get(0);
        assertEquals("key1", item1.getProperty());
        assertEquals(Integer.valueOf(1), item1.getS1_line_number());
        assertEquals("match \n  test", item1.getS1_value());
        assertEquals(false, item1.getS1_disabled());
        assertEquals(Integer.valueOf(1), item1.getS2_line_number());
        assertEquals("mismatch", item1.getS2_value());
        assertEquals(false, item1.getS2_disabled());
    }

    @Test
    public void testForCommentdProperties() throws IOException {
        String props1 = "key1=one\n"
            + "#key2=commented\n";
        String props2 = "#key1=one-commented\\\n"
            + " line\n"
            + "#key2=commented\n";

        List<PropertyDiffDetail> result = generateDiff(props1, props2);

        assertNotNull(result);
        assertEquals(2, result.size());

        PropertyDiffDetail item1 = result.get(0);
        assertEquals("key1", item1.getProperty());
        assertEquals(Integer.valueOf(1), item1.getS1_line_number());
        assertEquals("one", item1.getS1_value());
        assertEquals(false, item1.getS1_disabled());
        assertEquals(Integer.valueOf(1), item1.getS2_line_number());
        assertEquals("one-commented\n line", item1.getS2_value());
        assertEquals(true, item1.getS2_disabled());

        PropertyDiffDetail item2 = result.get(1);
        assertEquals("key2", item2.getProperty());
        assertEquals(Integer.valueOf(2), item2.getS1_line_number());
        assertEquals("commented", item2.getS1_value());
        assertEquals(true, item2.getS1_disabled());
        assertEquals(Integer.valueOf(3), item2.getS2_line_number());
        assertEquals("commented", item2.getS2_value());
        assertEquals(true, item2.getS2_disabled());
    }
}
