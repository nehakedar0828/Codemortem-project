package com.codemortem.controller;

import com.codemortem.dto.AIAnalysisResponseDTO;
import com.codemortem.dto.IncidentResponseDTO;
import com.codemortem.service.AIAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIAnalysisController {

    private final AIAnalysisService aiAnalysisService;

    @PostMapping("/analyze")
    public AIAnalysisResponseDTO analyzeIncident(
            @RequestBody IncidentResponseDTO dto
            ){

        return aiAnalysisService
                .analyzeIncident(
                        dto.getTitle(),
                        dto.getDescription()
                );
    }
}
