package org.configutils;

import java.util.Objects;

public class PropertyDiffDetail {
    private final String property;
    private final Integer s1_line_number;
    private final String s1_value;
    private final Boolean s1_disabled;
    private final Integer s2_line_number;
    private final String s2_value;
    private final Boolean s2_disabled;

    public PropertyDiffDetail(String key, PropertyValue v1, PropertyValue v2) {
        this.property = key;
        this.s1_line_number = (v1 == null) ? null : v1.getLineNumber();
        this.s1_value = (v1 == null) ? null : v1.getValue();
        this.s1_disabled = (v1 == null) ? null : !v1.isEnabled();
        this.s2_line_number = (v2 == null) ? null : v2.getLineNumber();
        this.s2_value = (v2 == null) ? null : v2.getValue();
        this.s2_disabled = (v2 == null) ? null : !v2.isEnabled();
    }

    public String getProperty() {
        return property;
    }

    public Integer getS1_line_number() {
        return s1_line_number;
    }

    public String getS1_value() {
        return s1_value;
    }

    public Boolean getS1_disabled() {
        return s1_disabled;
    }

    public Integer getS2_line_number() {
        return s2_line_number;
    }

    public String getS2_value() {
        return s2_value;
    }

    public Boolean getS2_disabled() {
        return s2_disabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyDiffDetail that = (PropertyDiffDetail) o;
        return property.equals(that.property) &&
            s1_line_number.equals(that.s1_line_number) &&
            s1_value.equals(that.s1_value) &&
            s1_disabled.equals(that.s1_disabled) &&
            s2_line_number.equals(that.s2_line_number) &&
            s2_value.equals(that.s2_value) &&
            s2_disabled.equals(that.s2_disabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, s1_line_number, s1_value, s1_disabled, s2_line_number, s2_value, s2_disabled);
    }

    @Override
    public String toString() {
        return "PropertyDiffDetail{" +
            "property='" + property + '\'' +
            ", s1_line_number=" + s1_line_number +
            ", s1_value='" + s1_value + '\'' +
            ", s1_disabled=" + s1_disabled +
            ", s2_line_number=" + s2_line_number +
            ", s2_value='" + s2_value + '\'' +
            ", s2_disabled=" + s2_disabled +
            '}';
    }
}
