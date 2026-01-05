package com.kg.impact;

import com.kg.diff.GraphDiffResult;
import com.kg.graph.Node;
import com.kg.graph.NodeType;

import java.util.HashSet;
import java.util.Set;

public class ChangedMethodExtractor {

    /**
     * Extracts IDs of all changed METHOD nodes
     * (added or removed).
     */
    public static Set<String> extract(GraphDiffResult diff) {

        Set<String> changedMethods = new HashSet<>();

        // added methods
        for (Node node : diff.nodesAdded) {
            if (node.type == NodeType.METHOD) {
                changedMethods.add(node.id);
            }
        }

        // removed methods
        for (Node node : diff.nodesRemoved) {
            if (node.type == NodeType.METHOD) {
                changedMethods.add(node.id);
            }
        }

        return changedMethods;
    }
}
