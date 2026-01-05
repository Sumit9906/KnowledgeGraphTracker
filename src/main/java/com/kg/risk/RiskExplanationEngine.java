package com.kg.risk;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates human-readable explanations for risk assessment
 * using numeric, deterministic rules.
 */
public class RiskExplanationEngine {

    public static RiskExplanation explain(
            RiskMetrics metrics,
            RiskScore score) {

        List<String> points = new ArrayList<>();

        // ---- Methods ----
        int methods = metrics.getImpactedMethodCount();
        if (methods > 0) {
            points.add(methods + " method(s) are impacted");
        }

        // ---- Classes (exclusive) ----
        int classes = metrics.getImpactedClassCount();
        if (classes == 0) {
            points.add("No external classes are affected");
        } else {
            points.add(classes + " external class(es) are affected");
        }

        // ---- Files (exclusive) ----
        int files = metrics.getImpactedFileCount();
        if (files == 0) {
            points.add("No external files are affected");
        } else {
            points.add(files + " external file(s) are affected");
        }

        // ---- Call depth ----
        int depth = metrics.getMaxTransitiveDepth();
        if (depth == 0) {
            points.add("Change does not propagate through call chains");
        } else {
            points.add("Maximum call-chain depth is " + depth);
        }

        return new RiskExplanation(score.getLevel(), points);
    }
}
