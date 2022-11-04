package com.example.lib;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class DefaultFileSearcher implements FileSearcher {

    private Path rootDir;
    private String wildcard;
    private Long sizeMinBytes;
    private Long sizeMaxBytes;
    private String contentRegex;

    @SneakyThrows
    @Override
    public List<File> find() {
        List<File> matchesList = new ArrayList<>();
        FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path filePath, BasicFileAttributes attribs) throws IOException {
                if (isMatch(filePath.toFile())) {
                    matchesList.add(filePath.toFile());
                }
                return FileVisitResult.CONTINUE;
            }
        };
        Files.walkFileTree(getRootDir(), matcherVisitor);
        return matchesList;
    }

    private boolean isMatch(File file) throws IOException {
        FileSystem fs = FileSystems.getDefault();
        PathMatcher matcher = fs.getPathMatcher(getWildcard());
        long size = file.length();
        if (getSizeMinBytes() != null && size < getSizeMinBytes()) {
            return false;
        } else if (getSizeMaxBytes() != null && size > getSizeMaxBytes()) {
            return false;
        } else if (getWildcard() != null && !matcher.matches(Path.of(file.getName()))) {
            return false;
        } else if (getContentRegex() != null) {
            String text = new String(Files.readAllBytes(Path.of(file.getPath())));
            return text.matches(getContentRegex());
        }

        return true;
    }

}
