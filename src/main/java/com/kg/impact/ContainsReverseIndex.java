package com.kg.impact;

import com.kg.graph.Edge;
import com.kg.graph.EdgeType;
import com.kg.graph.GraphStore;

import java.util.HashMap;
import java.util.Map;

public class ContainsReverseIndex {

    /**
     * Builds a reverse CONTAINS index:
     *   classId -> containingFileId
     */
    public static Map<String, String> build(GraphStore graph) {

        Map<String, String> classToFile = new HashMap<>();

        for (Edge edge : graph.getEdges()) {

            if (edge.type != EdgeType.CONTAINS) {
                continue;
            }

            String fileId = edge.from;
            String classId = edge.to;

            classToFile.put(classId, fileId);
        }

        return classToFile;
    }
}
