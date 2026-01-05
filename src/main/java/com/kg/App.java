package com.kg;

import com.kg.diff.GraphDiffEngine;
import com.kg.diff.GraphDiffPrinter;
import com.kg.diff.GraphDiffResult;
import com.kg.graph.GraphStore;
import com.kg.git.commit.CommitResolver;
import com.kg.impact.CallImpactAnalyzer;
import com.kg.impact.CallsReverseIndex;
import com.kg.impact.ChangedMethodExtractor;
import com.kg.impact.ContainsReverseIndex;
import com.kg.impact.DeclaresReverseIndex;
import com.kg.impact.ImpactPrinter;
import com.kg.impact.ImpactResult;
import com.kg.impact.StructuralImpactAnalyzer;
import com.kg.impact.TransitiveCallImpactAnalyzer;
import com.kg.snapshot.GraphSnapshotStore;
import com.kg.snapshot.SnapshotExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class App {

    public static void main(String[] args) {

        String repoPath =
                "C:/Users/divya yadav/PROJECT/KGraph/kg-cli/test-repo";

        try {

            // ================= DIFF COMMAND =================
            if (args.length >= 1 && args[0].equals("diff")) {

                boolean summaryOnly = false;
                List<String> positional = new ArrayList<>();

                for (String arg : args) {
                    if (arg.equals("--summary")) {
                        summaryOnly = true;
                    } else {
                        positional.add(arg);
                    }
                }

                String fromRef;
                String toRef;

                if (positional.size() == 2) {
                    fromRef = "HEAD~1";
                    toRef = positional.get(1);
                } else if (positional.size() == 3) {
                    fromRef = positional.get(1);
                    toRef = positional.get(2);
                } else {
                    System.err.println(
                            "Usage:\n" +
                            "  kg diff HEAD\n" +
                            "  kg diff <from> <to>\n" +
                            "  kg diff <from> <to> --summary"
                    );
                    System.exit(2);
                    return;
                }

                String fromCommit =
                        CommitResolver.resolveCommit(repoPath, fromRef);
                String toCommit =
                        CommitResolver.resolveCommit(repoPath, toRef);

                if (!GraphSnapshotStore.exists(fromCommit) ||
                    !GraphSnapshotStore.exists(toCommit)) {

                    System.err.println(
                            "Snapshot missing.\n\n" +
                            "How to fix:\n" +
                            "  git checkout <commit>\n" +
                            "  kg\n"
                    );
                    System.exit(2);
                    return;
                }

                GraphStore fromGraph =
                        GraphSnapshotStore.load(fromCommit);
                GraphStore toGraph =
                        GraphSnapshotStore.load(toCommit);

                GraphDiffResult diff =
                        GraphDiffEngine.diff(fromGraph, toGraph);

                GraphDiffPrinter.print(diff, summaryOnly);

                boolean hasChanges =
                        !diff.nodesAdded.isEmpty() ||
                        !diff.nodesRemoved.isEmpty() ||
                        !diff.edgesAdded.isEmpty() ||
                        !diff.edgesRemoved.isEmpty();

                System.exit(hasChanges ? 1 : 0);
                return;
            }

            // ================= IMPACT COMMAND =================
            if (args.length >= 1 && args[0].equals("impact")) {

                if (args.length != 3) {
                    System.err.println(
                            "Usage:\n" +
                            "  kg impact <from> <to>"
                    );
                    System.exit(2);
                    return;
                }

                String fromRef = args[1];
                String toRef = args[2];

                String fromCommit =
                        CommitResolver.resolveCommit(repoPath, fromRef);
                String toCommit =
                        CommitResolver.resolveCommit(repoPath, toRef);

                if (!GraphSnapshotStore.exists(fromCommit) ||
                    !GraphSnapshotStore.exists(toCommit)) {

                    System.err.println(
                            "Snapshot missing.\n\n" +
                            "How to fix:\n" +
                            "  git checkout <commit>\n" +
                            "  kg\n"
                    );
                    System.exit(2);
                    return;
                }

                GraphStore fromGraph =
                        GraphSnapshotStore.load(fromCommit);
                GraphStore toGraph =
                        GraphSnapshotStore.load(toCommit);


                // ---- Phase 4A pipeline ----
                GraphDiffResult diff =
                        GraphDiffEngine.diff(fromGraph, toGraph);

                Set<String> changedMethods =
                        ChangedMethodExtractor.extract(diff);

                Map<String, Set<String>> reverseCalls =
                        CallsReverseIndex.build(toGraph);

                Set<String> impactedMethods =
                        CallImpactAnalyzer.computeDirectImpact(
                                changedMethods, reverseCalls);                

                ImpactResult impact = new ImpactResult();
                
                impact.addAllMethods(impactedMethods);

                // Map<String, String> methodToClass = DeclaresReverseIndex.build(toGraph);
                // Map<String, String> classToFile = ContainsReverseIndex.build(toGraph);

                // StructuralImpactAnalyzer.compute(impact, methodToClass, classToFile);
                
                TransitiveCallImpactAnalyzer.compute(
                    changedMethods,
                    reverseCalls,
                    impact
                );


                ImpactPrinter.print(impact);
                System.exit(0);
                return;
            }

            // ================= NORMAL MODE =================
            GraphStore graph = SnapshotExecutor.execute(repoPath);
            graph.printSummary();
            System.exit(0);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(2);
        }
    }
}
