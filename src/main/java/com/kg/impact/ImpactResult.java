package com.kg.impact;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Holds impact analysis results.
 *
 * Phase 4A:
 *   - impacted methods
 *
 * Phase 4B:
 *   - impacted classes
 *   - impacted files
 *
 * Phase 4C:
 *   - transitive impacted methods by level
 */
public class ImpactResult {

    // ===== Phase 4A =====
    private final Set<String> impactedMethods = new TreeSet<>();

    // ===== Phase 4B =====
    private final Set<String> impactedClasses = new TreeSet<>();
    private final Set<String> impactedFiles = new TreeSet<>();

    // ===== Phase 4C =====
    // level -> impacted methods at that distance
    private final Map<Integer, Set<String>> transitiveMethodsByLevel =
            new TreeMap<>();

    // ---------------- METHODS ----------------

    public void addMethod(String methodId) {
        impactedMethods.add(methodId);
    }

    public void addAllMethods(Set<String> methodIds) {
        impactedMethods.addAll(methodIds);
    }

    public Set<String> getImpactedMethods() {
        return Collections.unmodifiableSet(impactedMethods);
    }

    // ---------------- CLASSES ----------------

    public void addClass(String classId) {
        impactedClasses.add(classId);
    }

    public void addAllClasses(Set<String> classIds) {
        impactedClasses.addAll(classIds);
    }

    public Set<String> getImpactedClasses() {
        return Collections.unmodifiableSet(impactedClasses);
    }

    // ---------------- FILES ----------------

    public void addFile(String fileId) {
        impactedFiles.add(fileId);
    }

    public void addAllFiles(Set<String> fileIds) {
        impactedFiles.addAll(fileIds);
    }

    public Set<String> getImpactedFiles() {
        return Collections.unmodifiableSet(impactedFiles);
    }

    // ---------------- TRANSITIVE (Phase 4C) ----------------

    public void addTransitiveMethod(int level, String methodId) {
        transitiveMethodsByLevel
                .computeIfAbsent(level, k -> new TreeSet<>())
                .add(methodId);
    }

    public void addAllTransitiveMethods(int level, Set<String> methodIds) {
        transitiveMethodsByLevel
                .computeIfAbsent(level, k -> new TreeSet<>())
                .addAll(methodIds);
    }

    public Map<Integer, Set<String>> getTransitiveMethodsByLevel() {
        return Collections.unmodifiableMap(transitiveMethodsByLevel);
    }

    // ---------------- HELPERS ----------------

    public boolean hasTransitiveImpact() {
        return !transitiveMethodsByLevel.isEmpty();
    }

    public boolean isEmpty() {
        return impactedMethods.isEmpty()
                && impactedClasses.isEmpty()
                && impactedFiles.isEmpty()
                && transitiveMethodsByLevel.isEmpty();
    }
}
