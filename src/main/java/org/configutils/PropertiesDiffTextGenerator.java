package org.configutils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PropertiesDiffTextGenerator extends PropertiesDiffGenerator {

    public static enum DiffCategory {
        MATCHING("Matching"),
        MISMATCH("Mismatch"),
        S1_ONLY("Only In Source1"),
        S2_ONLY("Only In Source2");

        private String value;

        private DiffCategory(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public PropertiesDiffTextGenerator() {
        this(null, null);
    }

    public PropertiesDiffTextGenerator(String file1, String file2) {
        super(file1, file2);
    }

    @Override
    public void generateReport() throws IOException {
        String text = generateText();
        System.out.println(text);
    }

    public String generateText() throws IOException {
        List<PropertyDiffDetail> diffDetails = super.generateDiff(new FileReader(file1), new FileReader(file2));
        return generateText(diffDetails);
    }

    public String generateText(List<PropertyDiffDetail> diffDetails) {
        String text = "";
        if (diffDetails == null) {
            return text;
        }

        Map<DiffCategory, List<PropertyDiffDetail>> categorizedDiffs = categorize(diffDetails);
        StringBuffer buffer = new StringBuffer();
        buffer.append(generatePropertiesText(DiffCategory.MATCHING, categorizedDiffs));
        buffer.append(generatePropertiesText(DiffCategory.S1_ONLY, categorizedDiffs));
        buffer.append(generatePropertiesText(DiffCategory.S2_ONLY, categorizedDiffs));
        buffer.append(generatePropertiesText(DiffCategory.MISMATCH, categorizedDiffs));

        return buffer.toString();
    }

    private String generatePropertiesText(DiffCategory category, Map<DiffCategory, List<PropertyDiffDetail>> categorizedDiffs) {
        List<PropertyDiffDetail> diffDetails = categorizedDiffs.get(category);
        StringBuffer buffer = new StringBuffer();
        if (diffDetails != null) {
            buffer.append("--- " + category.getValue() + " ---\n");
            diffDetails.forEach(detail -> {
                String value = (category == DiffCategory.S2_ONLY) ? detail.getS2_value(): detail.getS1_value();
                Boolean disabled = (category == DiffCategory.S2_ONLY) ? detail.getS2_disabled(): detail.getS1_disabled();
                if (category == DiffCategory.MISMATCH) {
                    buffer.append("> " + (detail.getS1_disabled() ? "# ": "") + detail.getProperty() + "=" + value + "\n");
                    buffer.append("< " + (detail.getS2_disabled() ? "# ": "") + detail.getProperty() + "=" + detail.getS2_value() + "\n");
                } else {
                    buffer.append((disabled ? "# ": "") + detail.getProperty() + "=" + value + "\n");
                }
            });
        }
        return buffer.toString();
    }

    private Map<DiffCategory, List<PropertyDiffDetail>> categorize(List<PropertyDiffDetail> diffDetails) {
        Map<DiffCategory, List<PropertyDiffDetail>> categorizedDiffs = new EnumMap<>(DiffCategory.class);
        diffDetails.forEach(diff -> {
            if (diff.getS1_value() != null && diff.getS2_value() != null) {
                if (diff.getS1_value().equals(diff.getS2_value()) && diff.getS1_disabled().equals(diff.getS2_disabled())) {
                    addTo(categorizedDiffs, DiffCategory.MATCHING, diff);
                } else {
                    addTo(categorizedDiffs, DiffCategory.MISMATCH, diff);

                }
            } else if (diff.getS1_value() == null && diff.getS2_value() == null) {
                addTo(categorizedDiffs, DiffCategory.MATCHING, diff);
            } else if (diff.getS1_value() != null) {
                addTo(categorizedDiffs, DiffCategory.S1_ONLY, diff);
            } else {
                addTo(categorizedDiffs, DiffCategory.S2_ONLY, diff);
            }
        });
        return categorizedDiffs;
    }

    private void addTo(Map<DiffCategory, List<PropertyDiffDetail>> categorizedDiffs, DiffCategory category, PropertyDiffDetail diff) {
        List<PropertyDiffDetail> details = categorizedDiffs.get(category);
        if (details == null) {
            details = new ArrayList<>();
            categorizedDiffs.put(category, details);
        }
        details.add(diff);
    }
}
