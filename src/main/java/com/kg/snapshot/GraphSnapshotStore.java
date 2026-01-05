package com.kg.snapshot;

import com.kg.graph.GraphStore;
import com.kg.output.GraphSerializer;

import java.io.File;

public class GraphSnapshotStore {

    private static final String DIR = "graphs";

    public static boolean exists(String commit) {
        return new File(DIR, commit + ".json").exists();
    }

    public static GraphStore load(String commit) throws Exception {
        return GraphSerializer.read(DIR + "/" + commit + ".json");
    }

    public static void save(String commit, GraphStore graph) throws Exception {
        File dir = new File(DIR);
        dir.mkdirs();

        File out = new File(dir, commit + ".json");
        if (out.exists()) {
            return; // immutable snapshot
        }

        GraphSerializer.write(graph, out.getPath());
    }
}
