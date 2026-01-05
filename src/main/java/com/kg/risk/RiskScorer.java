package com.kg.risk;

/**
 * Converts RiskMetrics into a numeric risk score and category.
 * Scoring rules are explainable and capped at 10.
 */
public class RiskScorer {

    public static RiskScore score(RiskMetrics metrics) {

        int score = 0;

        // ---- Impacted methods ----
        int methods = metrics.getImpactedMethodCount();
        if (methods >= 1 && methods <= 2) {
            score += 1;
        } else if (methods <= 5) {
            score += 2;
        } else if (methods > 5) {
            score += 3;
        }

        // ---- Impacted classes ----
        int classes = metrics.getImpactedClassCount();
        if (classes == 1) {
            score += 2;
        } else if (classes >= 2 && classes <= 3) {
            score += 3;
        } else if (classes > 3) {
            score += 4;
        }

        // ---- Impacted files ----
        int files = metrics.getImpactedFileCount();
        if (files == 1) {
            score += 3;
        } else if (files >= 2 && files <= 3) {
            score += 4;
        } else if (files > 3) {
            score += 5;
        }

        // ---- Transitive depth ----
        int depth = metrics.getMaxTransitiveDepth();
        if (depth == 1) {
            score += 1;
        } else if (depth == 2) {
            score += 2;
        } else if (depth == 3) {
            score += 3;
        } else if (depth >= 4) {
            score += 4;
        }

        // ---- Cap score ----
        score = Math.min(score, 10);

        // ---- Risk level ----
        RiskLevel level;
        if (score <= 2) {
            level = RiskLevel.LOW;
        } else if (score <= 6) {
            level = RiskLevel.MEDIUM;
        } else {
            level = RiskLevel.HIGH;
        }

        return new RiskScore(score, level);
    }
}
