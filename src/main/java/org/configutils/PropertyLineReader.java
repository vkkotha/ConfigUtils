package org.configutils;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

public class PropertyLineReader extends LineNumberReader {
    private StringBuffer currentPropertyLine;
    private int propertyLineNumber = 0;

    public PropertyLineReader(Reader in) {
        super(in);
    }

    @Override
    public String readLine() throws IOException {
        synchronized (lock) {
            String l = super.readLine();

            if (l != null) {
                if (l.endsWith("\\")) {
                    String pl = l.substring(0, l.length()-1);
                    if (currentPropertyLine == null) {
                        propertyLineNumber = super.getLineNumber();
                        currentPropertyLine = new StringBuffer(pl);
                    } else {
                        currentPropertyLine.append(System.getProperty("line.separator"));
                        currentPropertyLine.append(pl);
                    }
                    return readLine();
                } else {
                    if (currentPropertyLine != null) {
                        currentPropertyLine.append(System.getProperty("line.separator"));
                        currentPropertyLine.append(l);
                        l = currentPropertyLine.toString();
                        currentPropertyLine = null;
                    } else {
                        propertyLineNumber = super.getLineNumber();
                    }
                }
            } else {
                if (currentPropertyLine != null) {
                    l = currentPropertyLine.toString();
                    currentPropertyLine = null;
                }
            }
            return l;
        }
    }

    public int getPropertyLineNumber() {
        return propertyLineNumber;
    }
}
