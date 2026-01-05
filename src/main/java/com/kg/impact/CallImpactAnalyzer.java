package com.kg.impact;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CallImpactAnalyzer {

    /**
     * Computes direct call impact:
     * returns methods that directly CALL a changed method.
     */
    public static Set<String> computeDirectImpact(
            Set<String> changedMethods,
            Map<String, Set<String>> reverseCalls) {

        Set<String> impactedMethods = new HashSet<>();

        for (String changedMethod : changedMethods) {

            Set<String> callers =
                    reverseCalls.get(changedMethod);

            if (callers != null) {
                impactedMethods.addAll(callers);
            }
        }

        return impactedMethods;
    }
}
