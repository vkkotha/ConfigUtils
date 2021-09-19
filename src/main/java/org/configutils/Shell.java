package org.configutils;

import java.io.IOException;

public class Shell {
    public void exec(String command) throws IOException {
        Runtime.getRuntime().exec(command);
    }
}
