package com.archithreads.analyzer.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ThreadDumpAnalysis {
    private String dumpTimestamp;
    private String jvmVersion;
    private String jvmName;
    private String jvmArgs;
    private int totalThreads;
    private int runnableThreads;
    private int blockedThreads;
    private int waitingThreads;
    private int timedWaitingThreads;
    private int terminatedThreads;
    private List<ThreadInfo> threads;
    private List<ProblemPattern> topProblems;
    private Map<String, Integer> threadStateCounts;
    private Map<String, Integer> lockContentionCounts;
    private List<String> deadlockThreads;
    private List<String> highCpuThreads;
    private List<String> memoryLeakThreads;
    private String analysisSummary;

    public ThreadDumpAnalysis() {
        this.threads = new ArrayList<>();
        this.topProblems = new ArrayList<>();
        this.threadStateCounts = new HashMap<>();
        this.lockContentionCounts = new HashMap<>();
        this.deadlockThreads = new ArrayList<>();
        this.highCpuThreads = new ArrayList<>();
        this.memoryLeakThreads = new ArrayList<>();
    }

    // Getters and Setters
    public String getDumpTimestamp() { return dumpTimestamp; }
    public void setDumpTimestamp(String dumpTimestamp) { this.dumpTimestamp = dumpTimestamp; }

    public String getJvmVersion() { return jvmVersion; }
    public void setJvmVersion(String jvmVersion) { this.jvmVersion = jvmVersion; }

    public String getJvmName() { return jvmName; }
    public void setJvmName(String jvmName) { this.jvmName = jvmName; }

    public String getJvmArgs() { return jvmArgs; }
    public void setJvmArgs(String jvmArgs) { this.jvmArgs = jvmArgs; }

    public int getTotalThreads() { return totalThreads; }
    public void setTotalThreads(int totalThreads) { this.totalThreads = totalThreads; }

    public int getRunnableThreads() { return runnableThreads; }
    public void setRunnableThreads(int runnableThreads) { this.runnableThreads = runnableThreads; }

    public int getBlockedThreads() { return blockedThreads; }
    public void setBlockedThreads(int blockedThreads) { this.blockedThreads = blockedThreads; }

    public int getWaitingThreads() { return waitingThreads; }
    public void setWaitingThreads(int waitingThreads) { this.waitingThreads = waitingThreads; }

    public int getTimedWaitingThreads() { return timedWaitingThreads; }
    public void setTimedWaitingThreads(int timedWaitingThreads) { this.timedWaitingThreads = timedWaitingThreads; }

    public int getTerminatedThreads() { return terminatedThreads; }
    public void setTerminatedThreads(int terminatedThreads) { this.terminatedThreads = terminatedThreads; }

    public List<ThreadInfo> getThreads() { return threads; }
    public void setThreads(List<ThreadInfo> threads) { this.threads = threads; }

    public List<ProblemPattern> getTopProblems() { return topProblems; }
    public void setTopProblems(List<ProblemPattern> topProblems) { this.topProblems = topProblems; }

    public Map<String, Integer> getThreadStateCounts() { return threadStateCounts; }
    public void setThreadStateCounts(Map<String, Integer> threadStateCounts) { this.threadStateCounts = threadStateCounts; }

    public Map<String, Integer> getLockContentionCounts() { return lockContentionCounts; }
    public void setLockContentionCounts(Map<String, Integer> lockContentionCounts) { this.lockContentionCounts = lockContentionCounts; }

    public List<String> getDeadlockThreads() { return deadlockThreads; }
    public void setDeadlockThreads(List<String> deadlockThreads) { this.deadlockThreads = deadlockThreads; }

    public List<String> getHighCpuThreads() { return highCpuThreads; }
    public void setHighCpuThreads(List<String> highCpuThreads) { this.highCpuThreads = highCpuThreads; }

    public List<String> getMemoryLeakThreads() { return memoryLeakThreads; }
    public void setMemoryLeakThreads(List<String> memoryLeakThreads) { this.memoryLeakThreads = memoryLeakThreads; }

    public String getAnalysisSummary() { return analysisSummary; }
    public void setAnalysisSummary(String analysisSummary) { this.analysisSummary = analysisSummary; }
}
