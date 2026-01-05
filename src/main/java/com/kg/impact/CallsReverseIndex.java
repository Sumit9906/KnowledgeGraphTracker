package com.kg.impact;

import com.kg.graph.Edge;
import com.kg.graph.EdgeType;
import com.kg.graph.GraphStore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CallsReverseIndex {

    /**
     * Builds a reverse CALLS index:
     *   calleeMethodId -> set of callerMethodIds
     */
    public static Map<String, Set<String>> build(GraphStore graph) {

        Map<String, Set<String>> reverseCalls = new HashMap<>();

        for (Edge edge : graph.getEdges()) {

            if (edge.type != EdgeType.CALLS) {
                continue;
            }

            String caller = edge.from;
            String callee = edge.to;

            reverseCalls
                    .computeIfAbsent(callee, k -> new HashSet<>())
                    .add(caller);
        }

        return reverseCalls;
    }
}
