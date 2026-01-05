package com.kg.risk;

import com.kg.impact.ImpactResult;

import java.util.Map;
import java.util.Set;

/**
 * Holds raw blast-radius metrics for a change.
 * No scoring or categorization here.
 */
public class RiskMetrics {

    private final int impactedMethodCount;
    private final int impactedClassCount;
    private final int impactedFileCount;
    private final int maxTransitiveDepth;

    private RiskMetrics(
            int impactedMethodCount,
            int impactedClassCount,
            int impactedFileCount,
            int maxTransitiveDepth) {

        this.impactedMethodCount = impactedMethodCount;
        this.impactedClassCount = impactedClassCount;
        this.impactedFileCount = impactedFileCount;
        this.maxTransitiveDepth = maxTransitiveDepth;
    }

    // -------- FACTORY --------

    public static RiskMetrics fromImpact(ImpactResult impact) {

        int methodCount = impact.getImpactedMethods().size();
        int classCount = impact.getImpactedClasses().size();
        int fileCount = impact.getImpactedFiles().size();

        int maxDepth = 0;
        for (Map.Entry<Integer, Set<String>> entry
                : impact.getTransitiveMethodsByLevel().entrySet()) {

            if (!entry.getValue().isEmpty()) {
                maxDepth = Math.max(maxDepth, entry.getKey());
            }
        }

        return new RiskMetrics(
                methodCount,
                classCount,
                fileCount,
                maxDepth
        );
    }

    // -------- GETTERS --------

    public int getImpactedMethodCount() {
        return impactedMethodCount;
    }

    public int getImpactedClassCount() {
        return impactedClassCount;
    }

    public int getImpactedFileCount() {
        return impactedFileCount;
    }

    public int getMaxTransitiveDepth() {
        return maxTransitiveDepth;
    }
}
