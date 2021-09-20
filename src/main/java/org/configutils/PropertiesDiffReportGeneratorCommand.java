package org.configutils;

import java.io.IOException;

public class PropertiesDiffReportGeneratorCommand implements Command {
    public static enum ReportFormat {TEXT, HTML}

    private ReportFormat outFormat = ReportFormat.TEXT;

    public PropertiesDiffReportGeneratorCommand(String outputFormat) {
        if (outputFormat != null && outputFormat.equalsIgnoreCase(ReportFormat.HTML.name())) {
            this.outFormat = ReportFormat.HTML;
        }
    }

    @Override
    public void execute(String[] args) throws IOException {
        String file1 = args[0];
        String file2 = args[1];

        PropertiesDiffGenerator diffGenerator;
        if (this.outFormat == ReportFormat.HTML) {
            diffGenerator = new PropertiesDiffHtmlGenerator(file1, file2);
        } else {
            diffGenerator = new PropertiesDiffTextGenerator(file1, file2);
        }
        diffGenerator.generateReport();
    }
}
