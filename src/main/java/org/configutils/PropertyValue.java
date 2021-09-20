package org.configutils;

import java.util.Objects;

public class PropertyValue {

    private final String value;
    private final boolean enabled;
    private int lineNumber;

    public PropertyValue(String value) {
        this(value, true);
    }

    public PropertyValue(String value, boolean enabled) {
        this(value, enabled, 0);
    }

    public PropertyValue(String value, boolean enabled, int lineNumber) {
        this.value = value;
        this.enabled = enabled;
        this.lineNumber = lineNumber;

    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getValue() {
        return value;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyValue that = (PropertyValue) o;
        return enabled == that.enabled &&
            lineNumber == that.lineNumber &&
            Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, enabled, lineNumber);
    }

    @Override
    public String toString() {
        return "PropertyValue{" +
            "value='" + value + '\'' +
            ", enabled=" + enabled +
            ", lineNumber=" + lineNumber +
            '}';
    }
}
