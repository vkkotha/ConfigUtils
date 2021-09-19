package org.configutils;

import java.io.IOException;

public interface Command {
    void execute(String[] args) throws IOException;
}
