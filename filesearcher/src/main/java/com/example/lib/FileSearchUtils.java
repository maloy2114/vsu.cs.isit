package com.example.lib;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class FileSearchUtils {

    @Getter
    @Builder
    public static class FileSearchDescription {
        Path rootDir;
        String wildcard;
        Long sizeMinBytes;
        Long sizeMaxBytes;
        String contentRegex;
    }

    public List<File> find(FileSearchDescription description) throws IOException {
        List<File> matchesList = new ArrayList<>();
        FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path filePath, BasicFileAttributes attribs) throws IOException {
                if (isMatch(filePath.toFile(), description)) {
                    matchesList.add(filePath.toFile());
                }
                return FileVisitResult.CONTINUE;
            }
        };
        Files.walkFileTree(description.getRootDir(), matcherVisitor);
        return matchesList;
    }


    private boolean isMatch(File file, FileSearchDescription description) throws IOException {
        FileSystem fs = FileSystems.getDefault();
        PathMatcher matcher = fs.getPathMatcher(description.getWildcard());
        long size = file.length();
        if (description.getSizeMinBytes() != null && size < description.getSizeMinBytes()) {
            return false;
        } else if (description.getSizeMaxBytes() != null && size > description.getSizeMaxBytes()) {
            return false;
        } else if (description.getWildcard() != null && !matcher.matches(Path.of(file.getName()))){
            return false;
        } else if(description.getWildcard() != null){
            String text = new String(Files.readAllBytes(Path.of(file.getPath())));
            return text.matches(description.getContentRegex());
        }

        return true;
    }


}
