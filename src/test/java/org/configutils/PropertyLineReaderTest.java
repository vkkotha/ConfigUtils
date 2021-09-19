package org.configutils;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyLineReaderTest {
    @Test
    public void readSingleProperty() throws IOException {
        String text = "key1=value1";
        PropertyLineReader reader = new PropertyLineReader(new StringReader(text));
        String result = reader.readLine();
        assertEquals(text, result);
        assertEquals(1, reader.getLineNumber());
        assertEquals(1, reader.getPropertyLineNumber());
        result = reader.readLine();
        assertNull(result);
    }

    @Test
    public void readMultipleProperties() throws IOException {
        String text = "key1=value1\n"
            + "key2=value2";
        PropertyLineReader reader = new PropertyLineReader(new StringReader(text));
        String result = reader.readLine();
        assertEquals("key1=value1", result);
        assertEquals(1, reader.getLineNumber());
        assertEquals(1, reader.getPropertyLineNumber());
        result = reader.readLine();
        assertEquals("key2=value2", result);
        assertEquals(2, reader.getLineNumber());
        assertEquals(2, reader.getPropertyLineNumber());
    }

    @Test
    public void readPropertyLineWithContinuation() throws IOException {
        String text = "key1=one \\"
                + System.getProperty("line.separator")
                + "two";
        PropertyLineReader reader = new PropertyLineReader(new StringReader(text));
        String result = reader.readLine();
        assertEquals("key1=one \ntwo", result);
        assertEquals(2, reader.getLineNumber());
        assertEquals(1, reader.getPropertyLineNumber());
    }

    @Test
    public void readPropertyLineWithNewlineContinuation() throws IOException {
        String text = "key1=one \\\n"
                + "two";
        PropertyLineReader reader = new PropertyLineReader(new StringReader(text));
        String result = reader.readLine();
        assertEquals("key1=one \ntwo", result);
        assertEquals(2, reader.getLineNumber());
        assertEquals(1, reader.getPropertyLineNumber());
    }

    @Test
    public void readPropertyLinesWithMultipleContinuations() throws IOException {
        String text = "key1=one \\\n"
                + "two\n"
                + "key2=three \\\n"
                + "   four\n"
                + "key3=abc\\\n";
        PropertyLineReader reader = new PropertyLineReader(new StringReader(text));

        String result = reader.readLine();
        assertEquals("key1=one \ntwo", result);
        assertEquals(2, reader.getLineNumber());
        assertEquals(1, reader.getPropertyLineNumber());

        result = reader.readLine();
        assertEquals("key2=three \n   four", result);
        assertEquals(4, reader.getLineNumber());
        assertEquals(3, reader.getPropertyLineNumber());

        result = reader.readLine();
        assertEquals("key3=abc", result);
        assertEquals(5, reader.getLineNumber());
        assertEquals(5, reader.getPropertyLineNumber());
    }

    @Test
    public void readPropertyLinesWithDuplicates() throws Exception {
        String text = "key1=one\n"
                + "key2=two\n"
                + "key1=duplicate\n";
        PropertyLineReader reader = new PropertyLineReader(new StringReader(text));

        String result = reader.readLine();
        assertEquals("key1=one", result);
        assertEquals(1, reader.getLineNumber());
        assertEquals(1, reader.getPropertyLineNumber());

        result = reader.readLine();
        assertEquals("key2=two", result);
        assertEquals(2, reader.getLineNumber());
        assertEquals(2, reader.getPropertyLineNumber());

        result = reader.readLine();
        assertEquals("key1=duplicate", result);
        assertEquals(3, reader.getLineNumber());
        assertEquals(3, reader.getPropertyLineNumber());
    }

}
