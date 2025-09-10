package com.archithreads.analyzer.model;

import java.util.List;
import java.util.ArrayList;

public class ProblemPattern {
    private String patternName;
    private String description;
    private String severity;
    private int affectedThreads;
    private double confidence;
    private String rootCause;
    private String solution;
    private List<String> relatedThreads;
    private String patternType;
    private String detectionMethod;

    public ProblemPattern() {
        this.relatedThreads = new java.util.ArrayList<>();
    }

    public ProblemPattern(String patternName, String description, String severity, 
                         int affectedThreads, double confidence, String rootCause, String solution) {
        this();
        this.patternName = patternName;
        this.description = description;
        this.severity = severity;
        this.affectedThreads = affectedThreads;
        this.confidence = confidence;
        this.rootCause = rootCause;
        this.solution = solution;
    }

    // Getters and Setters
    public String getPatternName() { return patternName; }
    public void setPatternName(String patternName) { this.patternName = patternName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public int getAffectedThreads() { return affectedThreads; }
    public void setAffectedThreads(int affectedThreads) { this.affectedThreads = affectedThreads; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

    public String getRootCause() { return rootCause; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }

    public String getSolution() { return solution; }
    public void setSolution(String solution) { this.solution = solution; }

    public List<String> getRelatedThreads() { return relatedThreads; }
    public void setRelatedThreads(List<String> relatedThreads) { this.relatedThreads = relatedThreads; }

    public String getPatternType() { return patternType; }
    public void setPatternType(String patternType) { this.patternType = patternType; }

    public String getDetectionMethod() { return detectionMethod; }
    public void setDetectionMethod(String detectionMethod) { this.detectionMethod = detectionMethod; }
}
