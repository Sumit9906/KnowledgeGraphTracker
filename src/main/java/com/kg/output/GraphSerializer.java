package com.kg.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kg.graph.GraphStore;

import java.io.File;

public class GraphSerializer {

    // ---- WRITE GRAPH ----
    public static void write(GraphStore graph, String outputPath)
            throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        File outFile = new File(outputPath);
        outFile.getParentFile().mkdirs();

        mapper.writeValue(outFile, graph);
    }

    // ---- READ GRAPH (PHASE 1 FIX) ----
    public static GraphStore read(String path) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        File file = new File(path);

        // Case 1: file does not exist
        if (!file.exists()) {
            return new GraphStore();
        }

        // Case 2: file exists but is empty
        if (file.length() == 0) {
            return new GraphStore();
        }

        GraphStore graph = mapper.readValue(file, GraphStore.class);
        graph.rebuildIndexes();
        return graph;
    }
}
