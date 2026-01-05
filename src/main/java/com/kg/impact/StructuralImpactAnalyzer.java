package com.kg.impact;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StructuralImpactAnalyzer {

    /**
     * Computes structural impact:
     *   methods -> classes -> files
     */
    public static void compute(
            ImpactResult impact,
            Map<String, String> methodToClass,
            Map<String, String> classToFile) {

        Set<String> derivedClasses = new HashSet<>();
        Set<String> derivedFiles = new HashSet<>();

        // ---- METHODS -> CLASSES ----
        for (String methodId : impact.getImpactedMethods()) {
            String classId = methodToClass.get(methodId);
            if (classId != null) {
                derivedClasses.add(classId);
            }
        }

        // ---- CLASSES -> FILES ----
        for (String classId : derivedClasses) {
            String fileId = classToFile.get(classId);
            if (fileId != null) {
                derivedFiles.add(fileId);
            }
        }

        impact.addAllClasses(derivedClasses);
        impact.addAllFiles(derivedFiles);
    }
}
