package org.configutils;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PropertiesDiffToolCommand implements Command {
    private final Shell shell;
    public String diffExecutable;

    public PropertiesDiffToolCommand(Shell shell, String diffExecutable) {
        this.shell = shell;
        if (diffExecutable == null) {
            this.diffExecutable = "idea diff";
        } else {
            this.diffExecutable = diffExecutable;
        }
    }
    @Override
    public void execute(String[] args) throws IOException {
        String file1 = args[0];
        String file2 = args[1];

        PropertiesDiffGenerator diffGenerator = new PropertiesDiffGenerator() {
            @Override
            public void generateReport() throws IOException {
                // No need to implement
            }
        };

        List<PropertyDiffDetail> diffDetailList = diffGenerator.generateDiff(new FileReader(file1), new FileReader(file2));
        // Sort list by property name
        diffDetailList = diffDetailList.stream().sorted(Comparator.comparing(PropertyDiffDetail::getProperty))
                .collect(Collectors.toList());
        Path outFile1 = generateTempPropertiesFile(diffDetailList, "S1", file1);
        Path outFile2 = generateTempPropertiesFile(diffDetailList, "S2", file2);

        String cmd = diffExecutable + " " + outFile1.toAbsolutePath() + " " + outFile2.toAbsolutePath();
        System.out.println(cmd);
        shell.exec(cmd);
    }

    private Path generateTempPropertiesFile(List<PropertyDiffDetail> diffDetailList, String source, String sourceFile) throws IOException {
        String fileName = FilenameUtils.getBaseName(sourceFile);
        String fileExt = FilenameUtils.getExtension(sourceFile);
        if (fileExt != null) {
            fileExt = "." + fileExt;
        }
        Path tempFile = Files.createTempFile(fileName + "-", fileExt);

        try(BufferedWriter writer = Files.newBufferedWriter(tempFile, Charset.forName("UTF-8"))) {
            for (PropertyDiffDetail propertyDiffDetail : diffDetailList) {
                String key = propertyDiffDetail.getProperty();
                String value = propertyDiffDetail.getS1_value();
                Boolean disabled = propertyDiffDetail.getS1_disabled();
                if (source.equalsIgnoreCase("S2")) {
                    value = propertyDiffDetail.getS2_value();
                    disabled = propertyDiffDetail.getS2_disabled();
                }
                if (value != null) {
                    String propertyLine = key;
                    if (disabled != null && disabled) {
                        propertyLine = "# " + key;
                    }
                    propertyLine += "=" + value + "\n";
                    writer.write(propertyLine);
                } else {
                    // write empty line for missing properties
                    writer.write("-" + key + "=\n");
                }
            }
        }

        return tempFile;
    }
}
