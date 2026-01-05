package com.kg.risk;

/**
 * Holds final risk score and category.
 */
public class RiskScore {

    private final int score;
    private final RiskLevel level;

    public RiskScore(int score, RiskLevel level) {
        this.score = score;
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public RiskLevel getLevel() {
        return level;
    }
}
