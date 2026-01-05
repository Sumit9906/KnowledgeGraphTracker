package com.kg.graph;

public class Edge {

    public String from;
    public String to;
    public EdgeType type;

    public Edge() {}

    // REQUIRED constructor
    public Edge(String from, String to, EdgeType type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }
}
