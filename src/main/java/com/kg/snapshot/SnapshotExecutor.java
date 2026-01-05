package com.kg.snapshot;

import com.github.javaparser.ast.CompilationUnit;
import com.kg.extractor.ClassExtractor;
import com.kg.extractor.FileExtractor;
import com.kg.extractor.MethodExtractor;
import com.kg.git.FileChangeType;
import com.kg.git.GitDiffAnalyzer;
import com.kg.git.commit.CommitResolver;
import com.kg.graph.GraphStore;
import com.kg.graph.Node;
import com.kg.parser.JavaAstParser;
import com.kg.scanner.RepoScanner;

import java.io.File;
import java.util.List;
import java.util.Map;

public class SnapshotExecutor {

    public static GraphStore execute(String repoPath) throws Exception {

        String current = CommitResolver.getCurrentCommit(repoPath);

        // Case 1: snapshot already exists
        if (GraphSnapshotStore.exists(current)) {
            return GraphSnapshotStore.load(current);
        }

        String parent = CommitResolver.getParentCommit(repoPath);
        GraphStore graph;
        boolean incremental = false;

        if (parent != null && GraphSnapshotStore.exists(parent)) {
            graph = GraphSnapshotStore.load(parent);
            incremental = true;
        } else {
            graph = new GraphStore();
            fullBuild(repoPath, graph);
        }

        if (incremental) {
            incrementalBuild(repoPath, graph);
        }

        // ---- P2.1 + P2.2 METADATA ----
        graph.commitHash = current;
        graph.parentCommitHash = parent;
        graph.buildMode = incremental ? "INCREMENTAL" : "FULL";
        graph.generatedAt = System.currentTimeMillis();

        GraphSnapshotStore.save(current, graph);
        return graph;
    }

    private static void fullBuild(String repoPath, GraphStore graph)
            throws Exception {

        List<File> files = RepoScanner.getJavaFiles(repoPath);

        for (File file : files) {
            CompilationUnit cu = JavaAstParser.parse(file);
            FileExtractor.extract(file, cu, graph);
            ClassExtractor.extract(file, cu, graph);
            MethodExtractor.extract(cu, graph);
        }
    }

    private static void incrementalBuild(String repoPath, GraphStore graph)
            throws Exception {

        Map<String, FileChangeType> changes =
                GitDiffAnalyzer.getFileChanges(repoPath);

        for (Map.Entry<String, FileChangeType> entry :
                changes.entrySet()
                       .stream()
                       .sorted(Map.Entry.comparingByKey())
                       .toList()) {

            File file = new File(repoPath, entry.getKey());
            String fileId = file.getCanonicalPath();

            if (entry.getValue() == FileChangeType.DELETED) {
                graph.removeNodesByFile(fileId);
                continue;
            }

            CompilationUnit cu = JavaAstParser.parse(file);
            String newHash = FileExtractor.computeStructuralHash(cu);

            Node existing = graph.getNodes().stream()
                    .filter(n -> n.id.equals(fileId))
                    .findFirst()
                    .orElse(null);

            if (existing != null &&
                existing.contentHash != null &&
                existing.contentHash.equals(newHash)) {
                continue;
            }

            graph.removeNodesByFile(fileId);
            FileExtractor.extract(file, cu, graph);
            ClassExtractor.extract(file, cu, graph);
            MethodExtractor.extract(cu, graph);
        }
    }
}
