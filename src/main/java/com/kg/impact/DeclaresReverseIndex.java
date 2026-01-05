package com.kg.impact;

import com.kg.graph.Edge;
import com.kg.graph.EdgeType;
import com.kg.graph.GraphStore;

import java.util.HashMap;
import java.util.Map;

public class DeclaresReverseIndex {

    /**
     * Builds a reverse DECLARES index:
     *   methodId -> declaringClassId
     */
    public static Map<String, String> build(GraphStore graph) {

        Map<String, String> methodToClass = new HashMap<>();

        for (Edge edge : graph.getEdges()) {

            if (edge.type != EdgeType.DECLARES) {
                continue;
            }

            String methodId = edge.to;
            String classId = edge.from;

            methodToClass.put(methodId, classId);
        }

        return methodToClass;
    }
}
