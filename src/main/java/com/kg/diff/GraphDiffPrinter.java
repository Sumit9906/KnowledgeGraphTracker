package com.kg.diff;

import com.kg.graph.Edge;
import com.kg.graph.EdgeType;
import com.kg.graph.Node;
import com.kg.graph.NodeType;

import java.util.Comparator;
import java.util.List;

public class GraphDiffPrinter {

    public static void print(GraphDiffResult diff, boolean summaryOnly) {

        if (!summaryOnly) {
            printNodeSection("Nodes Added", "+", diff.nodesAdded);
            printNodeSection("Nodes Removed", "-", diff.nodesRemoved);

            printEdgeSection("Edges Added", "+", diff.edgesAdded);
            printEdgeSection("Edges Removed", "-", diff.edgesRemoved);
        }

        printSummary(diff);
    }

    // ---------------- SECTIONS ----------------

    private static void printNodeSection(
            String title, String prefix, List<Node> nodes) {

        if (nodes == null || nodes.isEmpty()) return;

        System.out.println();
        System.out.println(title + ":");

        nodes.stream()
                .sorted(nodeComparator())
                .forEach(n ->
                        System.out.printf(
                                "  %s %-7s %s%n",
                                prefix,
                                n.type.name(),
                                n.id
                        ));
    }

    private static void printEdgeSection(
            String title, String prefix, List<Edge> edges) {

        if (edges == null || edges.isEmpty()) return;

        System.out.println();
        System.out.println(title + ":");

        edges.stream()
                .sorted(edgeComparator())
                .forEach(e ->
                        System.out.printf(
                                "  %s %-8s %s → %s%n",
                                prefix,
                                e.type.name(),
                                e.from,
                                e.to
                        ));
    }

    // ---------------- ORDERING ----------------

    private static Comparator<Node> nodeComparator() {
        return Comparator
                .comparing((Node n) -> nodeRank(n.type))
                .thenComparing(n -> n.id);
    }

    private static Comparator<Edge> edgeComparator() {
        return Comparator
                .comparing((Edge e) -> edgeRank(e.type))
                .thenComparing(e -> e.from)
                .thenComparing(e -> e.to);
    }

    private static int nodeRank(NodeType type) {
        if (type == NodeType.FILE) return 0;
        if (type == NodeType.CLASS) return 1;
        if (type == NodeType.METHOD) return 2;
        return 99;
    }

    private static int edgeRank(EdgeType type) {
        if (type == EdgeType.CONTAINS) return 0;
        if (type == EdgeType.DECLARES) return 1;
        if (type == EdgeType.CALLS) return 2;
        return 99;
    }

    // ---------------- SUMMARY ----------------

    private static void printSummary(GraphDiffResult diff) {

        System.out.println();
        System.out.println("Summary:");
        System.out.println("  Nodes  +" + diff.nodesAdded.size()
                + " / -" + diff.nodesRemoved.size());
        System.out.println("  Edges  +" + diff.edgesAdded.size()
                + " / -" + diff.edgesRemoved.size());
    }
}
