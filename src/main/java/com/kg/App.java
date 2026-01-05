package com.kg;

import com.github.javaparser.ast.CompilationUnit;
import com.kg.extractor.ClassExtractor;
import com.kg.extractor.FileExtractor;
import com.kg.extractor.MethodExtractor;
import com.kg.git.FileChangeType;
import com.kg.git.GitDiffAnalyzer;
import com.kg.graph.GraphStore;
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

        graph.printSummary();
        GraphSerializer.write(graph, "output/graph.json");
    }

    private static void runFullBuild(String repoPath, GraphStore graph)
            throws Exception {

        List<File> files = RepoScanner.getJavaFiles(repoPath);

        for (File file : files) {
            CompilationUnit cu = JavaAstParser.parse(file);
            FileExtractor.extract(file, graph);
            ClassExtractor.extract(file, cu, graph);
            MethodExtractor.extract(cu, graph);
        }
    }

    private static void runIncremental(String repoPath, GraphStore graph)
            throws Exception {

        Map<String, FileChangeType> changes =
                GitDiffAnalyzer.getFileChanges(repoPath);

        for (Map.Entry<String, FileChangeType> entry : changes.entrySet()) {

            File file = new File(repoPath, entry.getKey());
            String canonicalPath = file.getCanonicalPath();

            switch (entry.getValue()) {

                case ADDED:
                case MODIFIED:
                    graph.removeNodesByFile(canonicalPath);
                    CompilationUnit cu = JavaAstParser.parse(file);
                    FileExtractor.extract(file, graph);
                    ClassExtractor.extract(file, cu, graph);
                    MethodExtractor.extract(cu, graph);
                    break;

                case DELETED:
                    graph.removeNodesByFile(canonicalPath);
                    break;
            }
        }
    }
}
