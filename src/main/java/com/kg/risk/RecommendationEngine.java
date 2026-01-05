package com.kg.risk;

/**
 * Provides actionable recommendations based on risk level.
 */
public class RecommendationEngine {

    public static String recommend(RiskLevel level) {

        switch (level) {
            case LOW:
                return "Safe to merge";

            case MEDIUM:
                return "Run targeted tests and review carefully";

            case HIGH:
                return "Run full regression tests and require careful review";

            default:
                // Defensive fallback (should never happen)
                return "Review change carefully";
        }
    }
}
