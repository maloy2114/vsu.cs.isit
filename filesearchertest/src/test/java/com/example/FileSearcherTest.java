package com.example;

import com.example.lib.DefaultFileSearcher;
import com.example.lib.FileSearcher;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class FileSearcherTest {

    @Test
    public void contentRegexTest() {
        FileSearcher fileSearcher = DefaultFileSearcher.builder()
                .rootDir(Paths.get("./testdir"))
                .contentRegex("hello")
                .sizeMinBytes(0L)
                .sizeMaxBytes(10L)
                .wildcard("glob:*")
                .build();

        List<File> files = fileSearcher.find();

        System.out.println(files);
        Assert.assertEquals(1, files.size());
        Assert.assertTrue(files.stream().anyMatch(f -> f.getName().equals("hellofile")));
    }

    @Test
    public void minSizeTest() {
        FileSearcher fileSearcher = DefaultFileSearcher.builder()
                .rootDir(Paths.get("./testdir"))
                .sizeMinBytes(53L)
                .sizeMaxBytes(100L)
                .wildcard("glob:*")
                .build();

        List<File> files = fileSearcher.find();

        System.out.println(files);
        Assert.assertEquals(1, files.size());
        Assert.assertTrue(files.stream().anyMatch(f -> f.getName().equals("54bytes")));
    }

    @Test
    public void maxSizeTest() {
        FileSearcher fileSearcher = DefaultFileSearcher.builder()
                .rootDir(Paths.get("./testdir"))
                .sizeMinBytes(0L)
                .sizeMaxBytes(0L)
                .wildcard("glob:*")
                .build();

        List<File> files = fileSearcher.find();

        System.out.println(files);
        Assert.assertEquals(1, files.size());
        Assert.assertTrue(files.stream().anyMatch(f -> f.getName().equals("empty")));
    }

    @Test
    public void globTest() {
        FileSearcher fileSearcher = DefaultFileSearcher.builder()
                .rootDir(Paths.get("./testdir"))
                .sizeMinBytes(0L)
                .sizeMaxBytes(100L)
                .contentRegex("qwerty")
                .wildcard("glob:*.xml")
                .build();

        List<File> files = fileSearcher.find();

        System.out.println(files);
        Assert.assertEquals(1, files.size());
        Assert.assertTrue(files.stream().anyMatch(f -> f.getName().equals("glob.xml")));
    }

}
