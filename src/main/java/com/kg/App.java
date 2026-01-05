package com.kg;

import com.github.javaparser.ast.CompilationUnit;
import com.kg.extractor.ClassExtractor;
import com.kg.extractor.FileExtractor;
import com.kg.extractor.MethodExtractor;
import com.kg.git.FileChangeType;
import com.kg.git.GitDiffAnalyzer;
import com.kg.graph.GraphStore;
import com.kg.graph.Node;
import com.kg.output.GraphSerializer;
import com.kg.parser.JavaAstParser;
import com.kg.scanner.RepoScanner;

import java.io.File;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {

        String repoPath =
                "C:/Users/divya yadav/PROJECT/KGraph/kg-cli/test-repo";

        GraphStore graph = GraphSerializer.read("output/graph.json");

        if (args.length > 0 && args[0].equals("diff")) {
            runIncremental(repoPath, graph);
        } else {
            runFullBuild(repoPath, graph);
        }

        graph.generatedAt = System.currentTimeMillis();
        graph.printSummary();
        GraphSerializer.write(graph, "output/graph.json");
    }

    private static void runFullBuild(String repoPath, GraphStore graph)
            throws Exception {

        List<File> files = RepoScanner.getJavaFiles(repoPath);

        for (File file : files) {
            CompilationUnit cu = JavaAstParser.parse(file);

            FileExtractor.extract(file, cu, graph);
            ClassExtractor.extract(file, cu, graph);
            MethodExtractor.extract(cu, graph);
        }
    }

    private static void runIncremental(String repoPath, GraphStore graph)
            throws Exception {

        Map<String, FileChangeType> changes =
                GitDiffAnalyzer.getFileChanges(repoPath);

        // 🔹 deterministic order (P1.1)
        for (Map.Entry<String, FileChangeType> entry :
                changes.entrySet()
                       .stream()
                       .sorted(Map.Entry.comparingByKey())
                       .toList()) {

            File file = new File(repoPath, entry.getKey());
            String fileId = file.getCanonicalPath();

            // 🔹 DELETE
            if (entry.getValue() == FileChangeType.DELETED) {
                graph.removeNodesByFile(fileId);
                continue;
            }

            // 🔹 ADDED / MODIFIED
            CompilationUnit cu = JavaAstParser.parse(file);

            // compute new structural hash
            String newHash =
                    FileExtractor.computeStructuralHash(cu);

            // get existing file node
            Node existingFileNode = graph.getNodes().stream()
                    .filter(n -> n.id.equals(fileId))
                    .findFirst()
                    .orElse(null);

            // 🔹 P1.3: skip unchanged structure
            if (existingFileNode != null &&
                existingFileNode.contentHash != null &&
                existingFileNode.contentHash.equals(newHash)) {
                continue;
            }

            // rebuild graph data for this file
            graph.removeNodesByFile(fileId);

            FileExtractor.extract(file, cu, graph);
            ClassExtractor.extract(file, cu, graph);
            MethodExtractor.extract(cu, graph);
        }
    }
}
