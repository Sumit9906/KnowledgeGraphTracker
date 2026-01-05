package com.kg.impact;

import java.util.Map;
import java.util.Set;

/**
 * CLI printer for impact analysis results.
 *
 * Phase 4A:
 *  - Impacted methods
 *
 * Phase 4B:
 *  - Impacted classes
 *  - Impacted files
 *
 * Phase 4C:
 *  - Transitive call impact (by level)
 */
public class ImpactPrinter {

    public static void print(ImpactResult result) {

        System.out.println();
        System.out.println("Impact Analysis:");

        // ---------- METHODS ----------
        System.out.println("\nImpacted Methods:");
        if (result.getImpactedMethods().isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (String method : result.getImpactedMethods()) {
                System.out.println("  " + method);
            }
        }

        // ---------- CLASSES ----------
        System.out.println("\nImpacted Classes:");
        if (result.getImpactedClasses().isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (String clazz : result.getImpactedClasses()) {
                System.out.println("  " + clazz);
            }
        }

        // ---------- FILES ----------
        System.out.println("\nImpacted Files:");
        if (result.getImpactedFiles().isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (String file : result.getImpactedFiles()) {
                System.out.println("  " + file);
            }
        }

        // ---------- TRANSITIVE IMPACT (Phase 4C) ----------
        if (result.hasTransitiveImpact()) {
            System.out.println("\nTransitive Impact (CALLS):");

            for (Map.Entry<Integer, Set<String>> entry
                    : result.getTransitiveMethodsByLevel().entrySet()) {

                System.out.println("\nLevel " + entry.getKey() + ":");
                for (String method : entry.getValue()) {
                    System.out.println("  " + method);
                }
            }
        }

        // ---------- SUMMARY ----------
        System.out.println("\nSummary:");
        System.out.println("  Methods: "
                + result.getImpactedMethods().size());
        System.out.println("  Classes: "
                + result.getImpactedClasses().size());
        System.out.println("  Files:   "
                + result.getImpactedFiles().size());
    }
}
