package org.configutils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesReader {

    private static final Pattern multilinePropertyRegex = Pattern.compile("^([\\s#]*)(.*?)=(.*)", Pattern.DOTALL);;

    public static Map.Entry<String, PropertyValue> parsePropertyString(String str) {
        Matcher matcher = multilinePropertyRegex.matcher(str);
        if (!matcher.find()) {
            return null;
        }

        boolean enabled = matcher.group(1).trim().startsWith("#") ? false: true;
        String key = matcher.group(2);
        String value = matcher.group(3).trim();
        PropertyValue propertyValue = new PropertyValue(value, enabled);
        return new AbstractMap.SimpleEntry<String, PropertyValue>(key, propertyValue);
    }

    public static Map<String, PropertyValue> loadProperties(String[] lines) throws IOException {
        byte[] input = String.join(System.lineSeparator(), Arrays.asList(lines)).getBytes();
        Reader reader = new InputStreamReader(new ByteArrayInputStream(input));
        return loadProperties(reader);
    }

    public static Map<String, PropertyValue> loadProperties(Reader reader) throws IOException {
        HashMap<String, PropertyValue> properties = new HashMap<>();
        PropertyLineReader propertyReader = getPropertyLineReader(reader);
        String line;
        while( (line = propertyReader.readLine()) != null ) {
            Map.Entry<String, PropertyValue> entry = parsePropertyString(line);
            if (entry != null) {
                entry.getValue().setLineNumber(propertyReader.getPropertyLineNumber());
                PropertyValue currentEntry = properties.get(entry.getKey());
                if (!shouldSkip(currentEntry, entry.getValue())) {
                    properties.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return properties;
    }

    private static PropertyLineReader getPropertyLineReader(Reader reader) {
        PropertyLineReader propertyReader = null;
        if (reader != null) {
            if (reader instanceof PropertyLineReader) {
                propertyReader = (PropertyLineReader) reader;
            } else {
                propertyReader = new PropertyLineReader(reader);
            }
        }
        return propertyReader;
    }

    private static boolean shouldSkip(PropertyValue currentValue, PropertyValue newValue) {
        return (currentValue != null && currentValue.isEnabled() && !newValue.isEnabled());
    }
}

