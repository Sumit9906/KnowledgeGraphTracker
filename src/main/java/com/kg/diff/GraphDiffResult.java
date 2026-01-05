package com.kg.diff;

import com.kg.graph.Edge;
import com.kg.graph.Node;

import java.util.ArrayList;
import java.util.List;

public class GraphDiffResult {

    // ---- METADATA ----
    public String fromCommit;
    public String toCommit;

    // ---- NODE DIFFS ----
    public List<Node> nodesAdded = new ArrayList<>();
    public List<Node> nodesRemoved = new ArrayList<>();

    // ---- EDGE DIFFS ----
    public List<Edge> edgesAdded = new ArrayList<>();
    public List<Edge> edgesRemoved = new ArrayList<>();

    public GraphDiffResult() {}

    public GraphDiffResult(String fromCommit, String toCommit) {
        this.fromCommit = fromCommit;
        this.toCommit = toCommit;
    }

    public void printSummary() {
        System.out.println("Diff Summary:");
        System.out.println("Nodes Added: " + nodesAdded.size());
        System.out.println("Nodes Removed: " + nodesRemoved.size());
        System.out.println("Edges Added: " + edgesAdded.size());
        System.out.println("Edges Removed: " + edgesRemoved.size());
    }
}
