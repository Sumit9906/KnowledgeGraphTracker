package com.kg.extractor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.kg.graph.EdgeType;
import com.kg.graph.GraphStore;
import com.kg.graph.NodeType;

import java.io.File;

public class ClassExtractor {

    public static void extract(File file, CompilationUnit cu, GraphStore graph) throws Exception {
        String packageName = cu.getPackageDeclaration().map(p -> p.getNameAsString()).orElse("default");

        String fileId = file.getCanonicalPath();

        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cls -> {

            String className = packageName + "." + cls.getNameAsString();

            // Class node
            graph.addNode(NodeType.CLASS, className, fileId);

            // File -> Class edge
            graph.addEdge(fileId, className, EdgeType.CONTAINS);
        });
    }
}
