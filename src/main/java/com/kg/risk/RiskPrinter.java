package com.kg.risk;

/**
 * CLI printer for risk assessment results.
 */
public class RiskPrinter {

    public static void print(RiskMetrics metrics, RiskScore score) {

        System.out.println("\nRisk Assessment:");
        System.out.println("  Score: " + score.getScore() + " / 10");
        System.out.println("  Level: " + score.getLevel());

        System.out.println("\nBlast Radius:");
        System.out.println("  Impacted Methods: " + metrics.getImpactedMethodCount());
        System.out.println("  Impacted Classes: " + metrics.getImpactedClassCount());
        System.out.println("  Impacted Files:   " + metrics.getImpactedFileCount());
        System.out.println("  Max Call Depth:   " + metrics.getMaxTransitiveDepth());
    }
}
