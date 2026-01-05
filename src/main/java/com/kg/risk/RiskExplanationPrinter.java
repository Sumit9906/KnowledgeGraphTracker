package com.kg.risk;

/**
 * Prints human-readable risk explanations and recommendations.
 */
public class RiskExplanationPrinter {

    public static void print(
            RiskExplanation explanation,
            String recommendation) {

        System.out.println();
        System.out.println("Why this is " + explanation.getLevel() + " risk:");

        for (String point : explanation.getPoints()) {
            System.out.println("- " + point);
        }

        System.out.println();
        System.out.println("Recommended Action:");
        System.out.println("- " + recommendation);
    }
}
