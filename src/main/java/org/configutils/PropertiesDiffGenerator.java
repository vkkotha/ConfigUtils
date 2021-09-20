package org.configutils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class PropertiesDiffGenerator {
    protected String file1;
    protected String file2;

    public PropertiesDiffGenerator() {
        this(null, null);
    }

    public PropertiesDiffGenerator(String file1, String file2) {
        this.file1 = file1;
        this.file2 = file2;
    }

    public List<PropertyDiffDetail> generateDiff(Reader reader1, Reader reader2) throws IOException {
        List<PropertyDiffDetail> details = new ArrayList<>();
        if (reader1 == null || reader2 == null) {
            return details;
        }

        Map<String, PropertyValue> props1 = PropertiesReader.loadProperties(reader1);
        Map<String, PropertyValue> props2 = PropertiesReader.loadProperties(reader2);

        Set<String> keys = new HashSet<>();
        keys.addAll(props1.keySet());
        keys.addAll(props2.keySet());

        keys.forEach(key -> {
            PropertyValue v1 = props1.get(key);
            PropertyValue v2 = props2.get(key);
            org.configutils.PropertyDiffDetail detail = new org.configutils.PropertyDiffDetail(key, v1, v2);
            details.add(detail);
        });

        return details;
    }

    public abstract void generateReport() throws IOException;

    public static String readResourceFile(String fileName) throws IOException {
        ClassLoader classLoader = PropertiesDiffGenerator.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            return result;
        }
    }
}
