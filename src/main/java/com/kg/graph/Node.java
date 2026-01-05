package com.kg.graph;

public class Node {
    public String contentHash;

    public String id;
    public NodeType type;

    public String fileId;

    public Node() {}

    // REQUIRED constructor
    public Node(String id, NodeType type, String fileId) {
        this.id = id;
        this.type = type;
        this.fileId = fileId;
    }
}
