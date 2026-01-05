package com.kg.git.commit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;

public class CommitResolver {

    public static String getCurrentCommit(String repoPath) throws Exception {
        return runGitCommand(repoPath, "git", "rev-parse", "HEAD");
    }

    public static String getParentCommit(String repoPath) throws Exception {
        try {
            return runGitCommand(repoPath, "git", "rev-parse", "HEAD~1");
        } catch (Exception e) {
            return null; // first commit
        }
    }


    public static String resolveCommit(String repoPath, String ref)
            throws Exception {
        return runGitCommand(repoPath, "git", "rev-parse", ref);
    }


    private static String runGitCommand(String repoPath, String... command)
            throws Exception {

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(repoPath)); // THIS IS THE KEY FIX
        pb.redirectErrorStream(true);

        Process p = pb.start();

        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(p.getInputStream()))) {

            String line = br.readLine();
            if (line == null || line.isBlank()) {
                throw new RuntimeException("Git command failed");
            }
            return line.trim();
        }
    }
}
