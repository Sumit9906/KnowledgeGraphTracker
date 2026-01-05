package com.kg;

import com.kg.diff.GraphDiffEngine;
import com.kg.diff.GraphDiffPrinter;
import com.kg.diff.GraphDiffResult;
import com.kg.graph.GraphStore;
import com.kg.git.commit.CommitResolver;
import com.kg.snapshot.GraphSnapshotStore;
import com.kg.snapshot.SnapshotExecutor;

import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {

        String repoPath =
                "C:/Users/divya yadav/PROJECT/KGraph/kg-cli/test-repo";

        // ---------------- DIFF MODE ----------------
        if (args.length >= 1 && args[0].equals("diff")) {

            boolean summaryOnly = false;
            String fromRef;
            String toRef;

            // collect non-flag args
            List<String> positional = new ArrayList<>();

            for (String arg : args) {
                if (arg.equals("--summary")) {
                    summaryOnly = true;
                } else {
                    positional.add(arg);
                }
            }

            if (positional.size() == 2) {
                // kg diff HEAD
                fromRef = "HEAD~1";
                toRef = positional.get(1);
            } else if (positional.size() == 3) {
                // kg diff <from> <to>
                fromRef = positional.get(1);
                toRef = positional.get(2);
            } else {
                throw new RuntimeException(
                        "Usage:\n" +
                        "  kg diff HEAD\n" +
                        "  kg diff <from> <to>\n" +
                        "  kg diff <from> <to> --summary"
                );
            }

            String fromCommit =
                    CommitResolver.resolveCommit(repoPath, fromRef);
            String toCommit =
                    CommitResolver.resolveCommit(repoPath, toRef);

            if (!GraphSnapshotStore.exists(fromCommit) ||
                !GraphSnapshotStore.exists(toCommit)) {

                throw new RuntimeException(
                        "Snapshot missing.\n\n" +
                        "How to fix:\n" +
                        "  git checkout <commit>\n" +
                        "  kg\n"
                );
            }

            GraphStore fromGraph =
                    GraphSnapshotStore.load(fromCommit);
            GraphStore toGraph =
                    GraphSnapshotStore.load(toCommit);

            GraphDiffResult diff =
                    GraphDiffEngine.diff(fromGraph, toGraph);

            GraphDiffPrinter.print(diff, summaryOnly);
            return;
        }

        // ---------------- NORMAL MODE ----------------
        GraphStore graph = SnapshotExecutor.execute(repoPath);
        graph.printSummary();
    }
}
