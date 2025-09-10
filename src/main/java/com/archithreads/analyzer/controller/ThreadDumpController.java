package com.archithreads.analyzer.controller;

import com.archithreads.analyzer.model.ThreadDumpAnalysis;
import com.archithreads.analyzer.service.ThreadDumpAnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class ThreadDumpController {

    @Autowired
    private ThreadDumpAnalyzerService analyzerService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/analyze")
    public String analyzeThreadDump(@RequestParam("threadDump") String threadDumpContent, 
                                   @RequestParam(value = "file", required = false) MultipartFile file,
                                   Model model) {
        try {
            String content = threadDumpContent;
            
            // 파일이 업로드된 경우 파일 내용 사용
            if (file != null && !file.isEmpty()) {
                content = new String(file.getBytes(), StandardCharsets.UTF_8);
            }
            
            // Thread Dump 분석 수행
            ThreadDumpAnalysis analysis = analyzerService.analyzeThreadDump(content);
            
            model.addAttribute("analysis", analysis);
            model.addAttribute("success", true);
            
        } catch (IOException e) {
            model.addAttribute("error", "파일 읽기 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", "분석 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return "result";
    }

    @GetMapping("/detailed")
    public String detailedView(@RequestParam("threadName") String threadName, Model model) {
        // 상세 보기를 위한 데이터 처리
        model.addAttribute("threadName", threadName);
        return "detailed";
    }

    @GetMapping("/patterns")
    public String patternsInfo(Model model) {
        return "patterns";
    }
}
