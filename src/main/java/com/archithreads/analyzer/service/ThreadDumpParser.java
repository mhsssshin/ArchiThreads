package com.archithreads.analyzer.service;

import com.archithreads.analyzer.model.ThreadInfo;
import com.archithreads.analyzer.model.ThreadDumpAnalysis;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ThreadDumpParser {

    private static final Pattern THREAD_HEADER_PATTERN = Pattern.compile(
        "\"([^\"]+)\"\\s+(?:#(\\d+)\\s+)?(?:daemon\\s+)?(?:prio=(\\d+)\\s+)?(?:os_prio=(\\d+)\\s+)?(?:tid=(0x[0-9a-fA-F]+)\\s+)?(?:nid=(0x[0-9a-fA-F]+)\\s+)?(?:nativeId=(0x[0-9a-fA-F]+)\\s+)?(?:javaThreadId=(\\d+)\\s+)?\\[([^\\]]+)\\]"
    );

    private static final Pattern LOCK_INFO_PATTERN = Pattern.compile(
        "-\\s+(?:waiting\\s+on|parking\\s+to\\s+wait\\s+for)\\s+<([^>]+)>\\s+\\(a\\s+([^)]+)\\)"
    );

    private static final Pattern LOCK_OWNER_PATTERN = Pattern.compile(
        "-\\s+locked\\s+<([^>]+)>\\s+\\(a\\s+([^)]+)\\)"
    );

    private static final Pattern BLOCKED_TIME_PATTERN = Pattern.compile(
        "java\\.lang\\.Thread\\.State:\\s+(\\w+)(?:\\s+\\(at\\s+([^)]+)\\))?"
    );

    public ThreadDumpAnalysis parseThreadDump(String threadDumpContent) {
        ThreadDumpAnalysis analysis = new ThreadDumpAnalysis();
        
        // JVM 정보 파싱
        parseJvmInfo(threadDumpContent, analysis);
        
        // 스레드 정보 파싱
        List<ThreadInfo> threads = parseThreads(threadDumpContent);
        analysis.setThreads(threads);
        
        // 통계 계산
        calculateStatistics(analysis);
        
        return analysis;
    }

    private void parseJvmInfo(String content, ThreadDumpAnalysis analysis) {
        // 타임스탬프 추출
        Pattern timestampPattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})");
        Matcher timestampMatcher = timestampPattern.matcher(content);
        if (timestampMatcher.find()) {
            analysis.setDumpTimestamp(timestampMatcher.group(1));
        }

        // JVM 버전 정보 추출
        Pattern jvmPattern = Pattern.compile("Java HotSpot\\(TM\\)\\s+(\\d+-Bit\\s+)?Server\\s+VM\\s+\\(([^)]+)\\)");
        Matcher jvmMatcher = jvmPattern.matcher(content);
        if (jvmMatcher.find()) {
            analysis.setJvmVersion(jvmMatcher.group(2));
        }

        // JVM 인수 추출
        Pattern argsPattern = Pattern.compile("Command line arguments:\\s*([^\\n]+)");
        Matcher argsMatcher = argsPattern.matcher(content);
        if (argsMatcher.find()) {
            analysis.setJvmArgs(argsMatcher.group(1));
        }
    }

    private List<ThreadInfo> parseThreads(String content) {
        List<ThreadInfo> threads = new ArrayList<>();
        String[] lines = content.split("\n");
        
        ThreadInfo currentThread = null;
        StringBuilder stackTrace = new StringBuilder();
        boolean inStackTrace = false;
        
        for (String line : lines) {
            line = line.trim();
            
            if (line.isEmpty()) {
                if (currentThread != null && inStackTrace) {
                    currentThread.setStackTrace(stackTrace.toString());
                    threads.add(currentThread);
                    currentThread = null;
                    stackTrace = new StringBuilder();
                    inStackTrace = false;
                }
                continue;
            }
            
            // 스레드 헤더 라인인지 확인
            Matcher headerMatcher = THREAD_HEADER_PATTERN.matcher(line);
            if (headerMatcher.matches()) {
                if (currentThread != null) {
                    currentThread.setStackTrace(stackTrace.toString());
                    threads.add(currentThread);
                }
                
                currentThread = new ThreadInfo();
                currentThread.setThreadName(headerMatcher.group(1));
                currentThread.setThreadId(headerMatcher.group(2));
                currentThread.setPriority(headerMatcher.group(3));
                currentThread.setOsPriority(headerMatcher.group(4));
                currentThread.setTid(headerMatcher.group(5));
                currentThread.setNid(headerMatcher.group(6));
                currentThread.setNativeId(headerMatcher.group(7));
                currentThread.setJavaThreadId(headerMatcher.group(8));
                currentThread.setThreadState(headerMatcher.group(9));
                
                stackTrace = new StringBuilder();
                inStackTrace = false;
                continue;
            }
            
            if (currentThread != null) {
                // 데몬 스레드 확인
                if (line.contains("daemon")) {
                    currentThread.setDaemon("true");
                }
                
                // 락 정보 파싱
                Matcher lockMatcher = LOCK_INFO_PATTERN.matcher(line);
                if (lockMatcher.matches()) {
                    currentThread.setLockInfo(lockMatcher.group(1));
                    currentThread.setWaitingOn(lockMatcher.group(1));
                    currentThread.setWaiting(true);
                }
                
                // 락 소유자 정보 파싱
                Matcher ownerMatcher = LOCK_OWNER_PATTERN.matcher(line);
                if (ownerMatcher.matches()) {
                    currentThread.getLockedMonitors().add(ownerMatcher.group(1));
                }
                
                // 스레드 상태 파싱
                Matcher stateMatcher = BLOCKED_TIME_PATTERN.matcher(line);
                if (stateMatcher.matches()) {
                    String state = stateMatcher.group(1);
                    currentThread.setJavaLangThreadState(state);
                    
                    switch (state) {
                        case "BLOCKED":
                            currentThread.setBlocked(true);
                            break;
                        case "WAITING":
                        case "TIMED_WAITING":
                            currentThread.setWaiting(true);
                            break;
                    }
                }
                
                // 스택 트레이스 수집
                if (line.startsWith("\tat ") || line.startsWith("\t- ")) {
                    inStackTrace = true;
                    currentThread.getStackFrames().add(line);
                    stackTrace.append(line).append("\n");
                }
            }
        }
        
        // 마지막 스레드 처리
        if (currentThread != null) {
            currentThread.setStackTrace(stackTrace.toString());
            threads.add(currentThread);
        }
        
        return threads;
    }

    private void calculateStatistics(ThreadDumpAnalysis analysis) {
        Map<String, Integer> stateCounts = new HashMap<>();
        int totalThreads = 0;
        int runnableThreads = 0;
        int blockedThreads = 0;
        int waitingThreads = 0;
        int timedWaitingThreads = 0;
        int terminatedThreads = 0;
        
        for (ThreadInfo thread : analysis.getThreads()) {
            totalThreads++;
            String state = thread.getJavaLangThreadState();
            
            if (state != null) {
                stateCounts.put(state, stateCounts.getOrDefault(state, 0) + 1);
                
                switch (state) {
                    case "RUNNABLE":
                        runnableThreads++;
                        break;
                    case "BLOCKED":
                        blockedThreads++;
                        break;
                    case "WAITING":
                        waitingThreads++;
                        break;
                    case "TIMED_WAITING":
                        timedWaitingThreads++;
                        break;
                    case "TERMINATED":
                        terminatedThreads++;
                        break;
                }
            }
        }
        
        analysis.setTotalThreads(totalThreads);
        analysis.setRunnableThreads(runnableThreads);
        analysis.setBlockedThreads(blockedThreads);
        analysis.setWaitingThreads(waitingThreads);
        analysis.setTimedWaitingThreads(timedWaitingThreads);
        analysis.setTerminatedThreads(terminatedThreads);
        analysis.setThreadStateCounts(stateCounts);
    }
}
