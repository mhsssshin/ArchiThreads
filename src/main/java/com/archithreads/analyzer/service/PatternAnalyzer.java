package com.archithreads.analyzer.service;

import com.archithreads.analyzer.model.ThreadInfo;
import com.archithreads.analyzer.model.ThreadDumpAnalysis;
import com.archithreads.analyzer.model.ProblemPattern;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PatternAnalyzer {

    private static final Map<String, Pattern> COMMON_PATTERNS = new HashMap<>();
    
    static {
        // 데이터베이스 관련 패턴
        COMMON_PATTERNS.put("DATABASE_DEADLOCK", Pattern.compile(".*java\\.sql\\.Connection.*|.*oracle\\.jdbc.*|.*mysql\\.jdbc.*|.*postgresql\\.jdbc.*"));
        COMMON_PATTERNS.put("DATABASE_TIMEOUT", Pattern.compile(".*SQLTimeoutException.*|.*Connection timeout.*|.*Query timeout.*"));
        COMMON_PATTERNS.put("DATABASE_POOL_EXHAUSTED", Pattern.compile(".*ConnectionPool.*|.*DataSource.*|.*HikariPool.*"));
        
        // 메모리 관련 패턴
        COMMON_PATTERNS.put("OUT_OF_MEMORY", Pattern.compile(".*OutOfMemoryError.*|.*GC overhead.*|.*PermGen.*|.*Metaspace.*"));
        COMMON_PATTERNS.put("MEMORY_LEAK", Pattern.compile(".*WeakReference.*|.*SoftReference.*|.*PhantomReference.*"));
        COMMON_PATTERNS.put("GC_PRESSURE", Pattern.compile(".*GC.*|.*GarbageCollector.*|.*Full GC.*"));
        
        // 네트워크 관련 패턴
        COMMON_PATTERNS.put("NETWORK_TIMEOUT", Pattern.compile(".*SocketTimeoutException.*|.*ConnectException.*|.*UnknownHostException.*"));
        COMMON_PATTERNS.put("HTTP_CLIENT_BLOCKED", Pattern.compile(".*HttpClient.*|.*ApacheHttpClient.*|.*OkHttp.*"));
        COMMON_PATTERNS.put("SOCKET_IO", Pattern.compile(".*SocketInputStream.*|.*SocketOutputStream.*"));
        
        // 동기화 관련 패턴
        COMMON_PATTERNS.put("DEADLOCK", Pattern.compile(".*deadlock.*|.*Found Java-level deadlock.*"));
        COMMON_PATTERNS.put("LOCK_CONTENTION", Pattern.compile(".*synchronized.*|.*ReentrantLock.*|.*ReadWriteLock.*"));
        COMMON_PATTERNS.put("WAIT_NOTIFY", Pattern.compile(".*wait\\(.*|.*notify\\(.*|.*notifyAll\\(.*"));
        
        // 스레드 풀 관련 패턴
        COMMON_PATTERNS.put("THREAD_POOL_EXHAUSTED", Pattern.compile(".*ThreadPoolExecutor.*|.*ForkJoinPool.*|.*ExecutorService.*"));
        COMMON_PATTERNS.put("THREAD_STARVATION", Pattern.compile(".*park.*|.*unpark.*|.*LockSupport.*"));
        
        // 파일 I/O 관련 패턴
        COMMON_PATTERNS.put("FILE_IO_BLOCKED", Pattern.compile(".*FileInputStream.*|.*FileOutputStream.*|.*RandomAccessFile.*"));
        COMMON_PATTERNS.put("NIO_BLOCKED", Pattern.compile(".*FileChannel.*|.*SocketChannel.*|.*Selector.*"));
        
        // 프레임워크 관련 패턴
        COMMON_PATTERNS.put("SPRING_BEAN_CREATION", Pattern.compile(".*BeanFactory.*|.*ApplicationContext.*|.*@Autowired.*"));
        COMMON_PATTERNS.put("HIBERNATE_SESSION", Pattern.compile(".*SessionFactory.*|.*Hibernate.*|.*EntityManager.*"));
        COMMON_PATTERNS.put("JPA_QUERY", Pattern.compile(".*JPQL.*|.*CriteriaQuery.*|.*TypedQuery.*"));
        
        // 캐시 관련 패턴
        COMMON_PATTERNS.put("CACHE_MISS", Pattern.compile(".*Cache.*|.*Redis.*|.*Memcached.*|.*EhCache.*"));
        COMMON_PATTERNS.put("CACHE_EVICTION", Pattern.compile(".*evict.*|.*expire.*|.*TTL.*"));
        
        // 메시징 관련 패턴
        COMMON_PATTERNS.put("MESSAGE_QUEUE", Pattern.compile(".*JMS.*|.*RabbitMQ.*|.*Kafka.*|.*ActiveMQ.*"));
        COMMON_PATTERNS.put("MESSAGE_CONSUMER", Pattern.compile(".*MessageConsumer.*|.*MessageListener.*"));
        
        // 로깅 관련 패턴
        COMMON_PATTERNS.put("LOGGING_BLOCKED", Pattern.compile(".*Logger.*|.*Log4j.*|.*Logback.*|.*SLF4J.*"));
        
        // 보안 관련 패턴
        COMMON_PATTERNS.put("SECURITY_MANAGER", Pattern.compile(".*SecurityManager.*|.*AccessController.*|.*PrivilegedAction.*"));
        
        // 직렬화 관련 패턴
        COMMON_PATTERNS.put("SERIALIZATION", Pattern.compile(".*ObjectInputStream.*|.*ObjectOutputStream.*|.*Serializable.*"));
        
        // 리플렉션 관련 패턴
        COMMON_PATTERNS.put("REFLECTION", Pattern.compile(".*Class\\.forName.*|.*Method\\.invoke.*|.*Constructor\\.newInstance.*"));
        
        // 컴파일 관련 패턴
        COMMON_PATTERNS.put("JIT_COMPILATION", Pattern.compile(".*Compiler.*|.*JIT.*|.*HotSpot.*"));
        
        // 모니터링 관련 패턴
        COMMON_PATTERNS.put("JMX_MONITORING", Pattern.compile(".*MBeanServer.*|.*JMX.*|.*ManagementFactory.*"));
        
        // 기타 시스템 패턴
        COMMON_PATTERNS.put("SYSTEM_PROPERTIES", Pattern.compile(".*System\\.getProperty.*|.*System\\.setProperty.*"));
        COMMON_PATTERNS.put("ENVIRONMENT_VARIABLES", Pattern.compile(".*System\\.getenv.*|.*ProcessBuilder.*"));
    }

    public List<ProblemPattern> analyzePatterns(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        // 각 패턴별로 분석 수행
        patterns.addAll(analyzeDeadlockPattern(analysis));
        patterns.addAll(analyzeLockContentionPattern(analysis));
        patterns.addAll(analyzeDatabasePattern(analysis));
        patterns.addAll(analyzeMemoryPattern(analysis));
        patterns.addAll(analyzeNetworkPattern(analysis));
        patterns.addAll(analyzeThreadPoolPattern(analysis));
        patterns.addAll(analyzeFileIOPattern(analysis));
        patterns.addAll(analyzeFrameworkPattern(analysis));
        patterns.addAll(analyzeCachePattern(analysis));
        patterns.addAll(analyzeMessagingPattern(analysis));
        patterns.addAll(analyzeLoggingPattern(analysis));
        patterns.addAll(analyzeSecurityPattern(analysis));
        patterns.addAll(analyzeSerializationPattern(analysis));
        patterns.addAll(analyzeReflectionPattern(analysis));
        patterns.addAll(analyzeJITPattern(analysis));
        patterns.addAll(analyzeMonitoringPattern(analysis));
        patterns.addAll(analyzeSystemPattern(analysis));
        
        // 심각도별로 정렬하고 상위 3개 반환
        return patterns.stream()
                .sorted((p1, p2) -> {
                    int severity1 = getSeverityScore(p1.getSeverity());
                    int severity2 = getSeverityScore(p2.getSeverity());
                    if (severity1 != severity2) {
                        return Integer.compare(severity2, severity1);
                    }
                    return Integer.compare(p2.getAffectedThreads(), p1.getAffectedThreads());
                })
                .limit(3)
                .collect(Collectors.toList());
    }

    private List<ProblemPattern> analyzeDeadlockPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        // 데드락 감지
        List<ThreadInfo> blockedThreads = analysis.getThreads().stream()
                .filter(t -> "BLOCKED".equals(t.getJavaLangThreadState()))
                .collect(Collectors.toList());
        
        if (blockedThreads.size() > 5) {
            ProblemPattern pattern = new ProblemPattern(
                "DEADLOCK_SUSPECTED",
                "데드락이 의심됩니다. " + blockedThreads.size() + "개의 스레드가 BLOCKED 상태입니다.",
                "HIGH",
                blockedThreads.size(),
                0.8,
                "스레드 간 락 경합으로 인한 데드락 발생",
                "락 순서를 일관성 있게 유지하고, 락 타임아웃을 설정하세요."
            );
            pattern.setRelatedThreads(blockedThreads.stream()
                    .map(ThreadInfo::getThreadName)
                    .collect(Collectors.toList()));
            patterns.add(pattern);
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeLockContentionPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        // 락 경합 분석
        Map<String, Integer> lockCounts = new HashMap<>();
        for (ThreadInfo thread : analysis.getThreads()) {
            if (thread.getLockInfo() != null) {
                lockCounts.put(thread.getLockInfo(), 
                    lockCounts.getOrDefault(thread.getLockInfo(), 0) + 1);
            }
        }
        
        for (Map.Entry<String, Integer> entry : lockCounts.entrySet()) {
            if (entry.getValue() > 3) {
                ProblemPattern pattern = new ProblemPattern(
                    "LOCK_CONTENTION",
                    "락 경합이 발생하고 있습니다. " + entry.getKey() + "에서 " + entry.getValue() + "개 스레드가 대기 중입니다.",
                    "MEDIUM",
                    entry.getValue(),
                    0.7,
                    "동일한 락에 대한 과도한 경합",
                    "락의 세분화를 고려하거나 락 없는 자료구조 사용을 검토하세요."
                );
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeDatabasePattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        for (ThreadInfo thread : analysis.getThreads()) {
            String stackTrace = thread.getStackTrace();
            
            if (COMMON_PATTERNS.get("DATABASE_DEADLOCK").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "DATABASE_DEADLOCK",
                    "데이터베이스 데드락이 발생했습니다.",
                    "HIGH",
                    1,
                    0.9,
                    "데이터베이스 트랜잭션 간 데드락",
                    "트랜잭션 순서를 일관성 있게 유지하고, 데드락 감지 및 해결 로직을 추가하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
            
            if (COMMON_PATTERNS.get("DATABASE_TIMEOUT").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "DATABASE_TIMEOUT",
                    "데이터베이스 타임아웃이 발생했습니다.",
                    "MEDIUM",
                    1,
                    0.8,
                    "데이터베이스 쿼리 타임아웃",
                    "쿼리 최적화 및 타임아웃 설정을 조정하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeMemoryPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        for (ThreadInfo thread : analysis.getThreads()) {
            String stackTrace = thread.getStackTrace();
            
            if (COMMON_PATTERNS.get("OUT_OF_MEMORY").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "OUT_OF_MEMORY",
                    "메모리 부족 오류가 발생했습니다.",
                    "CRITICAL",
                    1,
                    1.0,
                    "JVM 힙 메모리 부족",
                    "힙 메모리 크기를 증가시키거나 메모리 누수를 확인하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
            
            if (COMMON_PATTERNS.get("GC_PRESSURE").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "GC_PRESSURE",
                    "가비지 컬렉션 압박이 발생했습니다.",
                    "MEDIUM",
                    1,
                    0.6,
                    "과도한 가비지 컬렉션",
                    "GC 튜닝을 수행하고 메모리 사용량을 최적화하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeNetworkPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        for (ThreadInfo thread : analysis.getThreads()) {
            String stackTrace = thread.getStackTrace();
            
            if (COMMON_PATTERNS.get("NETWORK_TIMEOUT").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "NETWORK_TIMEOUT",
                    "네트워크 타임아웃이 발생했습니다.",
                    "MEDIUM",
                    1,
                    0.7,
                    "네트워크 연결 타임아웃",
                    "네트워크 타임아웃 설정을 조정하고 연결 풀링을 고려하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeThreadPoolPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        List<ThreadInfo> threadPoolThreads = analysis.getThreads().stream()
                .filter(t -> COMMON_PATTERNS.get("THREAD_POOL_EXHAUSTED").matcher(t.getStackTrace()).matches())
                .collect(Collectors.toList());
        
        if (threadPoolThreads.size() > 10) {
            ProblemPattern pattern = new ProblemPattern(
                "THREAD_POOL_EXHAUSTED",
                "스레드 풀이 고갈되었습니다. " + threadPoolThreads.size() + "개의 스레드 풀 스레드가 있습니다.",
                "HIGH",
                threadPoolThreads.size(),
                0.8,
                "스레드 풀 크기 부족",
                "스레드 풀 크기를 증가시키거나 작업 큐 크기를 조정하세요."
            );
            pattern.setRelatedThreads(threadPoolThreads.stream()
                    .map(ThreadInfo::getThreadName)
                    .collect(Collectors.toList()));
            patterns.add(pattern);
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeFileIOPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        for (ThreadInfo thread : analysis.getThreads()) {
            String stackTrace = thread.getStackTrace();
            
            if (COMMON_PATTERNS.get("FILE_IO_BLOCKED").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "FILE_IO_BLOCKED",
                    "파일 I/O가 블록되었습니다.",
                    "LOW",
                    1,
                    0.5,
                    "파일 시스템 I/O 지연",
                    "비동기 I/O 사용을 고려하거나 I/O 타임아웃을 설정하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeFrameworkPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        for (ThreadInfo thread : analysis.getThreads()) {
            String stackTrace = thread.getStackTrace();
            
            if (COMMON_PATTERNS.get("SPRING_BEAN_CREATION").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "SPRING_BEAN_CREATION",
                    "Spring Bean 생성 중 블록이 발생했습니다.",
                    "MEDIUM",
                    1,
                    0.6,
                    "Spring 컨텍스트 초기화 지연",
                    "Bean 의존성을 최적화하고 지연 초기화를 고려하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeCachePattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        for (ThreadInfo thread : analysis.getThreads()) {
            String stackTrace = thread.getStackTrace();
            
            if (COMMON_PATTERNS.get("CACHE_MISS").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "CACHE_MISS",
                    "캐시 미스가 발생했습니다.",
                    "LOW",
                    1,
                    0.4,
                    "캐시 효율성 저하",
                    "캐시 전략을 재검토하고 캐시 크기를 조정하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeMessagingPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        for (ThreadInfo thread : analysis.getThreads()) {
            String stackTrace = thread.getStackTrace();
            
            if (COMMON_PATTERNS.get("MESSAGE_QUEUE").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "MESSAGE_QUEUE_BLOCKED",
                    "메시지 큐 처리가 블록되었습니다.",
                    "MEDIUM",
                    1,
                    0.6,
                    "메시지 큐 처리 지연",
                    "메시지 큐 설정을 최적화하고 처리량을 조정하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeLoggingPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        for (ThreadInfo thread : analysis.getThreads()) {
            String stackTrace = thread.getStackTrace();
            
            if (COMMON_PATTERNS.get("LOGGING_BLOCKED").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "LOGGING_BLOCKED",
                    "로깅이 블록되었습니다.",
                    "LOW",
                    1,
                    0.3,
                    "로깅 시스템 지연",
                    "비동기 로깅을 사용하거나 로그 레벨을 조정하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeSecurityPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        for (ThreadInfo thread : analysis.getThreads()) {
            String stackTrace = thread.getStackTrace();
            
            if (COMMON_PATTERNS.get("SECURITY_MANAGER").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "SECURITY_MANAGER",
                    "보안 관리자 관련 블록이 발생했습니다.",
                    "MEDIUM",
                    1,
                    0.5,
                    "보안 정책 검사 지연",
                    "보안 정책을 최적화하거나 권한을 사전에 부여하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeSerializationPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        for (ThreadInfo thread : analysis.getThreads()) {
            String stackTrace = thread.getStackTrace();
            
            if (COMMON_PATTERNS.get("SERIALIZATION").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "SERIALIZATION_BLOCKED",
                    "직렬화/역직렬화가 블록되었습니다.",
                    "LOW",
                    1,
                    0.4,
                    "객체 직렬화 지연",
                    "직렬화 최적화를 고려하거나 비동기 처리를 사용하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeReflectionPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        for (ThreadInfo thread : analysis.getThreads()) {
            String stackTrace = thread.getStackTrace();
            
            if (COMMON_PATTERNS.get("REFLECTION").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "REFLECTION_OVERUSE",
                    "리플렉션 사용이 과도합니다.",
                    "LOW",
                    1,
                    0.3,
                    "리플렉션 성능 오버헤드",
                    "리플렉션 사용을 최소화하고 캐싱을 고려하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeJITPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        for (ThreadInfo thread : analysis.getThreads()) {
            String stackTrace = thread.getStackTrace();
            
            if (COMMON_PATTERNS.get("JIT_COMPILATION").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "JIT_COMPILATION",
                    "JIT 컴파일이 진행 중입니다.",
                    "LOW",
                    1,
                    0.2,
                    "JIT 컴파일 오버헤드",
                    "JIT 컴파일 최적화를 고려하거나 AOT 컴파일을 사용하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeMonitoringPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        for (ThreadInfo thread : analysis.getThreads()) {
            String stackTrace = thread.getStackTrace();
            
            if (COMMON_PATTERNS.get("JMX_MONITORING").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "JMX_MONITORING",
                    "JMX 모니터링이 활성화되어 있습니다.",
                    "LOW",
                    1,
                    0.1,
                    "모니터링 오버헤드",
                    "모니터링 설정을 최적화하거나 샘플링 주기를 조정하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private List<ProblemPattern> analyzeSystemPattern(ThreadDumpAnalysis analysis) {
        List<ProblemPattern> patterns = new ArrayList<>();
        
        for (ThreadInfo thread : analysis.getThreads()) {
            String stackTrace = thread.getStackTrace();
            
            if (COMMON_PATTERNS.get("SYSTEM_PROPERTIES").matcher(stackTrace).matches()) {
                ProblemPattern pattern = new ProblemPattern(
                    "SYSTEM_PROPERTIES_ACCESS",
                    "시스템 프로퍼티 접근이 발생했습니다.",
                    "LOW",
                    1,
                    0.2,
                    "시스템 프로퍼티 접근 오버헤드",
                    "시스템 프로퍼티를 캐싱하여 성능을 개선하세요."
                );
                pattern.setRelatedThreads(Arrays.asList(thread.getThreadName()));
                patterns.add(pattern);
            }
        }
        
        return patterns;
    }

    private int getSeverityScore(String severity) {
        switch (severity) {
            case "CRITICAL": return 5;
            case "HIGH": return 4;
            case "MEDIUM": return 3;
            case "LOW": return 2;
            default: return 1;
        }
    }
}
