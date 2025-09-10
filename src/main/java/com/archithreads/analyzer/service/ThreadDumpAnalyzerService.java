package com.archithreads.analyzer.service;

import com.archithreads.analyzer.model.ThreadDumpAnalysis;
import com.archithreads.analyzer.model.ProblemPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThreadDumpAnalyzerService {

    @Autowired
    private ThreadDumpParser parser;

    @Autowired
    private PatternAnalyzer patternAnalyzer;

    public ThreadDumpAnalysis analyzeThreadDump(String threadDumpContent) {
        // Thread Dump 파싱
        ThreadDumpAnalysis analysis = parser.parseThreadDump(threadDumpContent);
        
        // 패턴 분석 수행
        List<ProblemPattern> patterns = patternAnalyzer.analyzePatterns(analysis);
        
        // 상위 3개 문제점 설정
        analysis.setTopProblems(patterns.stream().limit(3).collect(Collectors.toList()));
        
        // 분석 요약 생성
        analysis.setAnalysisSummary(generateAnalysisSummary(analysis, patterns));
        
        return analysis;
    }

    private String generateAnalysisSummary(ThreadDumpAnalysis analysis, List<ProblemPattern> patterns) {
        StringBuilder summary = new StringBuilder();
        
        summary.append("=== Thread Dump 분석 결과 ===\n");
        summary.append("총 스레드 수: ").append(analysis.getTotalThreads()).append("\n");
        summary.append("RUNNABLE: ").append(analysis.getRunnableThreads()).append("개\n");
        summary.append("BLOCKED: ").append(analysis.getBlockedThreads()).append("개\n");
        summary.append("WAITING: ").append(analysis.getWaitingThreads()).append("개\n");
        summary.append("TIMED_WAITING: ").append(analysis.getTimedWaitingThreads()).append("개\n");
        summary.append("TERMINATED: ").append(analysis.getTerminatedThreads()).append("개\n\n");
        
        if (!patterns.isEmpty()) {
            summary.append("=== 주요 문제점 TOP 3 ===\n");
            for (int i = 0; i < Math.min(3, patterns.size()); i++) {
                ProblemPattern pattern = patterns.get(i);
                summary.append((i + 1)).append(". ").append(pattern.getPatternName())
                       .append(" (").append(pattern.getSeverity()).append(")\n");
                summary.append("   설명: ").append(pattern.getDescription()).append("\n");
                summary.append("   영향받는 스레드: ").append(pattern.getAffectedThreads()).append("개\n");
                summary.append("   신뢰도: ").append(String.format("%.1f%%", pattern.getConfidence() * 100)).append("\n");
                summary.append("   해결방안: ").append(pattern.getSolution()).append("\n\n");
            }
        } else {
            summary.append("심각한 문제점이 발견되지 않았습니다.\n");
        }
        
        // 추가 권장사항
        summary.append("=== 권장사항 ===\n");
        if (analysis.getBlockedThreads() > 5) {
            summary.append("- BLOCKED 스레드가 많습니다. 락 경합을 확인하세요.\n");
        }
        if (analysis.getWaitingThreads() > 10) {
            summary.append("- WAITING 스레드가 많습니다. 대기 시간을 확인하세요.\n");
        }
        if (analysis.getTotalThreads() > 100) {
            summary.append("- 전체 스레드 수가 많습니다. 스레드 풀 설정을 검토하세요.\n");
        }
        
        return summary.toString();
    }
}
