package com.codemortem.service;

import com.codemortem.dto.AIAnalysisResponseDTO;
import com.codemortem.dto.OpenRouterRequestDTO;
import com.codemortem.dto.OpenRouterResponseDTO;
import com.codemortem.entity.Incident;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AIAnalysisService {

    private final RestTemplate restTemplate;

    @Value("${openrouter.api.key}")
    private String apiKey;

    public AIAnalysisResponseDTO analyzeIncident(

            String title,
            String description,
            String affectedService,
            Object severity) {

        String prompt = """
You are a senior Site Reliability Engineer (SRE) and production incident response expert.

Analyze the following software production incident professionally.

Provide your response STRICTLY in this format:

Incident Summary:
- Briefly summarize the issue.

Most Likely Root Causes:
- Mention the most probable technical causes.

Immediate Mitigation Steps:
- Suggest urgent actions to stabilize the system.

Debugging Checklist:
- Provide step-by-step debugging guidance.

Long-Term Preventive Fixes:
- Suggest architectural or operational improvements.

Risk Assessment:
- Mention the potential business or system impact if unresolved.

Keep the response practical, concise, and production-focused.
Avoid generic explanations.

Incident Details:

Title:
%s

Description:
%s

Affected Service:
%s

Severity:
%s
""".formatted(
                title,
                description,
                affectedService,
                severity
        );

        OpenRouterRequestDTO.Message message =
                new OpenRouterRequestDTO.Message(
                        "user",
                        prompt
                );

        OpenRouterRequestDTO requestBody =
                new OpenRouterRequestDTO(
                        "deepseek/deepseek-chat",
                        List.of(message)
                );

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        headers.setBearerAuth(apiKey);

        HttpEntity<OpenRouterRequestDTO> entity =
                new HttpEntity<>(
                        requestBody,
                        headers
                );

        String url =
                "https://openrouter.ai/api/v1/chat/completions";

        try {

            ResponseEntity<OpenRouterResponseDTO> response =
                    restTemplate.postForEntity(
                            url,
                            entity,
                            OpenRouterResponseDTO.class
                    );

            String analysis =
                    response.getBody()
                            .getChoices()
                            .get(0)
                            .getMessage()
                            .getContent();

            return new AIAnalysisResponseDTO(
                    analysis
            );

        } catch (Exception e) {

            e.printStackTrace();

            return new AIAnalysisResponseDTO(
                    e.getMessage()
            );
        }
    }
}