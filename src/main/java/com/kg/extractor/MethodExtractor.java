package com.kg.extractor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.kg.graph.EdgeType;
import com.kg.graph.GraphStore;
import com.kg.graph.NodeType;

public class MethodExtractor {

    public static void extract(CompilationUnit cu, GraphStore graph) {

        String packageName = cu.getPackageDeclaration()
                .map(p -> p.getNameAsString())
                .orElse("default");

        cu.findAll(MethodDeclaration.class).forEach(method -> {

            method.findAncestor(ClassOrInterfaceDeclaration.class)
                  .ifPresent(cls -> {

                      String classId =
                              packageName + "." + cls.getNameAsString();

                      String callerMethodId =
                              classId + "." + method.getNameAsString();

                        String fileId = graph.getNodes().stream()
                              .filter(n ->
                                      n.id.equals(classId))
                              .findFirst()
                              .map(n -> n.fileId)
                              .orElse(null);

                        if (fileId == null) return;

                      // Method node
                      graph.addNode(NodeType.METHOD, callerMethodId, fileId);

                      // Class -> Method edge
                      graph.addEdge(
                              classId,
                              callerMethodId,
                              EdgeType.DECLARES
                      );

                      // Method CALL edges
                      method.findAll(MethodCallExpr.class).forEach(call -> {

                          String calleeMethodId =
                                  classId + "." + call.getNameAsString();

                          graph.addNode(NodeType.METHOD, calleeMethodId, fileId);

                          graph.addEdge(
                                  callerMethodId,
                                  calleeMethodId,
                                  EdgeType.CALLS
                          );
                      });
                  });
        });
    }
}
