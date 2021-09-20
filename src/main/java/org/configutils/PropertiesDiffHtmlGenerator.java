package org.configutils;

import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class PropertiesDiffHtmlGenerator extends PropertiesDiffGenerator {

    public PropertiesDiffHtmlGenerator() {
        this(null, null);
    }

    public PropertiesDiffHtmlGenerator(String file1, String file2) {
        super(file1, file2);
    }

    @Override
    public void generateReport() throws IOException {
        String markup = this.generateMarkup();
        System.out.println(markup);
    }

    public String generateMarkup() throws IOException {
        List<PropertyDiffDetail> diffDetails = super.generateDiff(new FileReader(file1), new FileReader(file2));

        String markup = generateMarkup(diffDetails);
        return markup;

    }

    public String generateMarkup(List<PropertyDiffDetail> diffDetails) throws IOException {
        String json = new Gson().toJson(diffDetails);
        String template = readResourceFile("properties_diff_template.html");
        String markup = template.replaceAll("__PROPS_DATA__", json);

        String source1 = this.file1 != null ? FilenameUtils.getName(this.file1) : "Source1";
        String source2 = this.file1 != null ? FilenameUtils.getName(this.file2) : "Source1";
        markup = markup.replaceAll("__SOURCE1__", source1);
        markup = markup.replaceAll("__SOURCE2__", source2);

        return markup;
    }

}
