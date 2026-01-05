package com.kg.diff;

import com.kg.graph.Edge;
import com.kg.graph.GraphStore;
import com.kg.graph.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphDiffEngine {

    public static GraphDiffResult diff(
            GraphStore fromGraph,
            GraphStore toGraph
    ) {

        GraphDiffResult result = new GraphDiffResult(
                fromGraph.commitHash,
                toGraph.commitHash
        );

        // ---- NODE DIFF ----
        Map<String, Node> fromNodes = indexNodes(fromGraph);
        Map<String, Node> toNodes = indexNodes(toGraph);

        for (String id : toNodes.keySet()) {
            if (!fromNodes.containsKey(id)) {
                result.nodesAdded.add(toNodes.get(id));
            }
        }

        for (String id : fromNodes.keySet()) {
            if (!toNodes.containsKey(id)) {
                result.nodesRemoved.add(fromNodes.get(id));
            }
        }

        // ---- EDGE DIFF ----
        Set<String> fromEdges = indexEdges(fromGraph);
        Set<String> toEdges = indexEdges(toGraph);

        for (String e : toEdges) {
            if (!fromEdges.contains(e)) {
                result.edgesAdded.add(parseEdge(e));
            }
        }

        for (String e : fromEdges) {
            if (!toEdges.contains(e)) {
                result.edgesRemoved.add(parseEdge(e));
            }
        }

        return result;
    }

    // ---------------- HELPERS ----------------

    private static Map<String, Node> indexNodes(GraphStore graph) {
        Map<String, Node> map = new HashMap<>();
        for (Node n : graph.getNodes()) {
            // identity = id + type
            map.put(nodeKey(n), n);
        }
        return map;
    }

    private static Set<String> indexEdges(GraphStore graph) {
        Set<String> set = new HashSet<>();
        for (Edge e : graph.getEdges()) {
            set.add(edgeKey(e));
        }
        return set;
    }

    private static String nodeKey(Node n) {
        return n.type + "|" + n.id;
    }

    private static String edgeKey(Edge e) {
        return e.from + "|" + e.type + "|" + e.to;
    }

    private static Edge parseEdge(String key) {
        String[] parts = key.split("\\|", 3);
        return new Edge(
                parts[0],
                parts[2],
                com.kg.graph.EdgeType.valueOf(parts[1])
        );
    }
}
