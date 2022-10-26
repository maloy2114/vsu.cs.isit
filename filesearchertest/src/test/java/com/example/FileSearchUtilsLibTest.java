package com.example;

import com.example.lib.FileSearchUtils;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FileSearchUtilsLibTest extends TestCase {

    public void testFind() throws IOException {
        FileSearchUtils.FileSearchDescription fileSearchDescription = FileSearchUtils.FileSearchDescription.builder()
                .rootDir(Path.of("."))
                .contentRegex(".*")
                .sizeMaxBytes(10L)
                .wildcard("glob:*")
                .build();

        List<File> files = FileSearchUtils.find(fileSearchDescription);
        System.out.println(files);
        Assert.assertTrue(files.stream().anyMatch(f -> f.getName().equals("testfile")));
    }
}
