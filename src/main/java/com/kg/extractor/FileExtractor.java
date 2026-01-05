package com.kg.extractor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.kg.graph.GraphStore;
import com.kg.graph.NodeType;

import java.io.File;
import java.security.MessageDigest;

public class FileExtractor {
    public static String computeStructuralHash(CompilationUnit cu)
        throws Exception {
    return buildStructuralHash(cu);
}

    public static void extract(
            File file,
            CompilationUnit cu,
            GraphStore graph
    ) throws Exception {

        String fileId = file.getCanonicalPath();
        String structuralHash = buildStructuralHash(cu);

        graph.addNode(NodeType.FILE, fileId, fileId);

        graph.getNodes().stream()
             .filter(n -> n.id.equals(fileId))
             .findFirst()
             .ifPresent(n -> n.contentHash = structuralHash);
    }

    // STRUCTURAL SIGNATURE (AST-BASED)
    private static String buildStructuralHash(CompilationUnit cu)
            throws Exception {

        StringBuilder sb = new StringBuilder();

        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cls -> {
            sb.append("C:").append(cls.getNameAsString()).append(";");

            cls.getMethods().forEach(m -> {
                sb.append("M:")
                  .append(m.getNameAsString())
                  .append("(");

                m.findAll(MethodCallExpr.class).forEach(call ->
                        sb.append(call.getNameAsString()).append(",")
                );

                sb.append(");");
            });
        });

        return sha256(sb.toString());
    }

    private static String sha256(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes());

        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
