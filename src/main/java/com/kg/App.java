package com.kg;

import com.kg.graph.GraphStore;
import com.kg.snapshot.SnapshotExecutor;

public class App {

    public static void main(String[] args) throws Exception {

        String repoPath =
                "C:/Users/divya yadav/PROJECT/KGraph/kg-cli/test-repo";

        GraphStore graph = SnapshotExecutor.execute(repoPath);

        graph.printSummary();
    }
}
