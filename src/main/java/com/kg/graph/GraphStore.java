package com.kg.graph;

import java.util.*;

public class GraphStore {

    // ---- JSON-SERIALIZED FIELDS ----
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    // ---- INTERNAL INDEXES (NOT SERIALIZED) ----
    private transient Map<String, Node> nodeIndex = new HashMap<>();
    private transient Set<String> edgeKeys = new HashSet<>();

    // Required by Jackson
    public GraphStore() {}

    // ---- NODE OPERATIONS ----
    public void addNode(NodeType type, String id, String fileId) {
        if (!nodeIndex.containsKey(id)) {
            Node node = new Node(id, type, fileId);
            nodes.add(node);
            nodeIndex.put(id, node);
        }
    }

    // ---- EDGE OPERATIONS ----
    public void addEdge(String from, String to, EdgeType type) {
        String key = from + "|" + type + "|" + to;
        if (!edgeKeys.contains(key)) {
            edges.add(new Edge(from, to, type));
            edgeKeys.add(key);
        }
    }

    // ---- REMOVAL (PHASE 1) ----
    public void removeNodesByFile(String fileId) {

    // 1️⃣ Collect node IDs that belong to the file
    Set<String> doomedNodeIds = new HashSet<>();

    for (Node n : nodes) {
        if (fileId.equals(n.fileId)) {
            doomedNodeIds.add(n.id);
        }
    }

    // 2️⃣ Remove edges connected to those nodes
    edges.removeIf(e ->
            doomedNodeIds.contains(e.from) ||
            doomedNodeIds.contains(e.to)
    );

    // 3️⃣ Remove the nodes themselves
    nodes.removeIf(n -> fileId.equals(n.fileId));

    // 4️⃣ Rebuild indexes
    rebuildIndexes();
}

    // ---- REBUILD INTERNAL INDEXES ----
    public void rebuildIndexes() {
        nodeIndex.clear();
        edgeKeys.clear();

        for (Node n : nodes) {
            nodeIndex.put(n.id, n);
        }
        for (Edge e : edges) {
            edgeKeys.add(e.from + "|" + e.type + "|" + e.to);
        }
    }

    // ---- GETTERS (USED BY JACKSON) ----
    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    // ---- DEBUG ----
    public void printSummary() {
        System.out.println("Nodes: " + nodes.size());
        System.out.println("Edges: " + edges.size());
    }
}
