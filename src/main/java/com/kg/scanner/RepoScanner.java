package com.kg.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RepoScanner {

    public static List<File> getJavaFiles(String repoPath) {
        List<File> files = new ArrayList<>();
        File root = new File(repoPath);

        if (!root.exists() || !root.isDirectory()) {
            throw new IllegalArgumentException(
                "Invalid repo path: " + repoPath
            );
        }

        scan(root, files);
        return files;
    }

    private static void scan(File dir, List<File> files) {
        File[] children = dir.listFiles();

        if (children == null) return; // 👈 IMPORTANT

        for (File file : children) {
            if (file.isDirectory()) {
                scan(file, files);
            } else if (file.getName().endsWith(".java")) {
                files.add(file);
            }
        }
    }
}
