package com.kg.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class GitDiffAnalyzer {

    public static Map<String, FileChangeType> getFileChanges(String repoPath) throws Exception {

        ProcessBuilder pb = new ProcessBuilder(
                "git", "diff", "--name-status", "HEAD~1", "HEAD"
        );
        pb.directory(new File(repoPath));

        Process process = pb.start();

        Map<String, FileChangeType> changes = new HashMap<>();

        try (BufferedReader reader =
                     new BufferedReader(
                         new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length < 2) continue;
                String status = parts[0];
                String filePath = parts[1];

                if (!filePath.endsWith(".java")) continue;

                switch (status) {
                    case "A":
                        changes.put(filePath, FileChangeType.ADDED);
                        break;
                    case "M":
                        changes.put(filePath, FileChangeType.MODIFIED);
                        break;
                    case "D":
                        changes.put(filePath, FileChangeType.DELETED);
                        break;
                }
            }
        }

        return changes;
    }
}
