package org.configutils;

public class CommandFactory {
    public Command getPropertiesReportGeneratorCommand(String outputFormat) {
        return new PropertiesDiffReportGeneratorCommand(outputFormat);
    }

    public Command getPropertiesDiffToolCommand(String diffExecutable) {
        return new PropertiesDiffToolCommand(new Shell(), diffExecutable);
    }
}
