package com.kg.impact;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class TransitiveCallImpactAnalyzer {

    /**
     * Computes transitive call impact using BFS.
     *
     * @param changedMethods   starting methods (not included in result levels)
     * @param reverseCalls     callee -> callers index
     * @param impact           ImpactResult to populate
     */
    public static void compute(
            Set<String> changedMethods,
            Map<String, Set<String>> reverseCalls,
            ImpactResult impact) {

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        // initialize BFS with changed methods
        for (String method : changedMethods) {
            queue.add(method);
            visited.add(method);
        }

        int level = 1;

        // BFS traversal by levels
        while (!queue.isEmpty()) {

            int levelSize = queue.size();
            Set<String> levelMethods = new HashSet<>();

            for (int i = 0; i < levelSize; i++) {
                String current = queue.poll();

                Set<String> callers = reverseCalls.get(current);
                if (callers == null) {
                    continue;
                }

                for (String caller : callers) {
                    if (visited.contains(caller)) {
                        continue;
                    }

                    visited.add(caller);
                    levelMethods.add(caller);
                    queue.add(caller);
                }
            }

            if (!levelMethods.isEmpty()) {
                impact.addAllTransitiveMethods(level, levelMethods);
            }

            level++;
        }
    }
}
