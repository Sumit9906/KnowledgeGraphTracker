package com.kg.risk;

import java.util.Collections;
import java.util.List;

/**
 * Holds explanation points for a risk assessment.
 */
public class RiskExplanation {

    private final RiskLevel level;
    private final List<String> points;

    public RiskExplanation(RiskLevel level, List<String> points) {
        this.level = level;
        this.points = points;
    }

    public RiskLevel getLevel() {
        return level;
    }

    public List<String> getPoints() {
        return Collections.unmodifiableList(points);
    }
}
