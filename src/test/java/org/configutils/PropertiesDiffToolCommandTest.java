package org.configutils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class PropertiesDiffToolCommandTest {
    @Test
    public void shouldInvokeDiffExecutable() throws IOException {
        Shell mockShell = mock(Shell.class);
        String diffExecutable = "meld";
        PropertiesDiffToolCommand command = new PropertiesDiffToolCommand(mockShell, diffExecutable);
        File testResources = new File("src/test/resources");
        final File file1 = new File(testResources, "test1.properties");
        final File file2 = new File(testResources, "test2.properties");

        command.execute(new String[] {file1.getAbsolutePath(), file2.getAbsolutePath()});
        verify(mockShell, times(1)).exec(contains(diffExecutable));
    }
}
