package org.configutils;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

public class PropertiesReaderTest {
    @Test
    public void parseString() {
        String propertyStr = "key=value";

        Map.Entry<String, PropertyValue> result = PropertiesReader.parsePropertyString(propertyStr);

        String expectedKey = "key";
        PropertyValue expectedPropertyValue = new PropertyValue("value");

        assertNotNull(result);
        assertEquals(expectedKey, result.getKey());
        assertEquals(expectedPropertyValue, result.getValue());
    }

    @Test
    public void parseCommentedString() {
        String propertyStr = " ## key=value";

        Map.Entry<String, PropertyValue> result = PropertiesReader.parsePropertyString(propertyStr);

        String expectedKey = "key";
        PropertyValue expectedPropertyValue = new PropertyValue("value", false);

        assertNotNull(result);
        assertEquals(expectedKey, result.getKey());
        assertEquals(expectedPropertyValue, result.getValue());
    }

    @Test
    public void parseStringWithMultipleLines() {
        String propertyStr = "key=value\n"
            + "second value\r"
            + "third value\r\n"
            + "fourth value \\n"
            + "fifth value";

        Map.Entry<String, PropertyValue> result = PropertiesReader.parsePropertyString(propertyStr);

        String expectedKey = "key";
        PropertyValue expectedPropertyValue = new PropertyValue("value\n"
            + "second value\r"
            + "third value\r\n"
            + "fourth value \\n"
            + "fifth value");

        assertNotNull(result);
        assertEquals(expectedKey, result.getKey());
        assertEquals(expectedPropertyValue, result.getValue());
    }

    @Test
    public void parseLines() throws IOException {
        String[] lines = new String[]{
            "key1=value1",
            "key2=value2"
        };

        Map<String, PropertyValue> result = PropertiesReader.loadProperties(lines);
        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.containsKey("key1"));
        assertEquals(new PropertyValue("value1", true, 1), result.get("key1"));

        assertTrue(result.containsKey("key2"));
        assertEquals(new PropertyValue("value2", true, 2), result.get("key2"));
    }

    @Test
    public void parseLinesWithComments() throws IOException {
        String[] lines = new String[]{
            "key1=value1",
            "#key2=value2"
        };

        Map<String, PropertyValue> result = PropertiesReader.loadProperties(lines);
        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.containsKey("key1"));
        assertEquals(new PropertyValue("value1", true, 1), result.get("key1"));

        assertTrue(result.containsKey("key2"));
        assertEquals(new PropertyValue("value2", false, 2), result.get("key2"));
    }

    @Test
    public void parseLinesWithBadProperties() throws IOException {
        String[] lines = new String[]{
            "key1=value1",
            "abc",
            "",
            "#key2=value2"
        };

        Map<String, PropertyValue> result = PropertiesReader.loadProperties(lines);
        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.containsKey("key1"));
        assertEquals(new PropertyValue("value1", true, 1), result.get("key1"));

        assertTrue(result.containsKey("key2"));
        assertEquals(new PropertyValue("value2", false, 4), result.get("key2"));
    }

    @Test
    public void parseLinesWithMultilineContinuation() throws IOException {
        String[] lines = new String[]{
            "key1=one \\",
            "two"
        };

        Map<String, PropertyValue> result = PropertiesReader.loadProperties(lines);
        assertNotNull(result);
        assertEquals(1, result.size());

        assertTrue(result.containsKey("key1"));
        assertEquals(new PropertyValue("one \ntwo", true, 1), result.get("key1"));
    }

    @Test
    public void parseLinesWithCommendsAndMultilineContinuation() throws IOException {
        String[] lines = new String[]{
            "key1=one \\",
            "two",
            "key2=three \\",
            "   four",
            "key3=abc",
            "#key4=abc \\",
            "def",
            "key5=v5"
        };

        Map<String, PropertyValue> result = PropertiesReader.loadProperties(lines);
        assertNotNull(result);
        assertEquals(5, result.size());

        assertTrue(result.containsKey("key1"));
        assertEquals(new PropertyValue("one \ntwo", true, 1), result.get("key1"));

        assertTrue(result.containsKey("key2"));
        assertEquals(new PropertyValue("three \n   four", true, 3), result.get("key2"));

        assertTrue(result.containsKey("key3"));
        assertEquals(new PropertyValue("abc", true, 5), result.get("key3"));

        assertTrue(result.containsKey("key4"));
        assertEquals(new PropertyValue("abc \ndef", false, 6), result.get("key4"));

        assertTrue(result.containsKey("key5"));
        assertEquals(new PropertyValue("v5", true, 8), result.get("key5"));
    }

    @Test
    public void parseLinesWithDuplicates() throws IOException {
        String[] lines = new String[]{
            "key1=one",
            "key2=two",
            "key1=duplicate"
        };

        Map<String, PropertyValue> result = PropertiesReader.loadProperties(lines);
        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.containsKey("key1"));
        assertEquals(new PropertyValue("duplicate", true, 3), result.get("key1"));

        assertTrue(result.containsKey("key2"));
        assertEquals(new PropertyValue("two", true, 2), result.get("key2"));
    }

    @Test
    public void parseLinesWithDuplicatesWithSomeCommented() throws IOException {
        String[] lines = new String[]{
            "key1=one",
            "key1=one_override",
            "key2=two",
            "#key2=two_skip",
            "#key3=three",
            "key3=three_override_comment",
            "key4=four",
            "key5=five \\",
            " six",
            "#key4=four_comment",
            "key4=four_final"
        };

        Map<String, PropertyValue> result = PropertiesReader.loadProperties(lines);
        assertNotNull(result);
        assertEquals(5, result.size());

        assertTrue(result.containsKey("key1"));
        assertEquals(new PropertyValue("one_override", true, 2), result.get("key1"));

        assertTrue(result.containsKey("key2"));
        assertEquals(new PropertyValue("two", true, 3), result.get("key2"));

        assertTrue(result.containsKey("key3"));
        assertEquals(new PropertyValue("three_override_comment", true, 6), result.get("key3"));

        assertTrue(result.containsKey("key4"));
        assertEquals(new PropertyValue("four_final", true, 11), result.get("key4"));

        assertTrue(result.containsKey("key5"));
        assertEquals(new PropertyValue("five \n six", true, 8), result.get("key5"));
    }

    @Test
    public void parseLinesWithEqualsInValue() throws IOException {
        String[] lines = new String[]{
            "key1=one=equals-in=value",
            "key2==equals-at-begining",
        };

        Map<String, PropertyValue> result = PropertiesReader.loadProperties(lines);
        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.containsKey("key1"));
        assertEquals(new PropertyValue("one=equals-in=value", true, 1), result.get("key1"));

        assertTrue(result.containsKey("key2"));
        assertEquals(new PropertyValue("=equals-at-begining", true, 2), result.get("key2"));

    }
}
