package com.codemortem.service;

import com.codemortem.dto.AIAnalysisResponseDTO;
import com.codemortem.dto.OpenRouterRequestDTO;
import com.codemortem.dto.OpenRouterResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AIAnalysisService {

    private final RestTemplate restTemplate;

    @Value("${openrouter.api.key}")
    private String apiKey;

    public AIAnalysisResponseDTO analyzeIncident(

            String title,
            String description) {

        String prompt = """
You are an incident analysis assistant.

Analyze the incident below and return ONLY plain text.

Do not use:
- markdown
- stars
- hashtags
- bullet points
- bold formatting

Incident Title:
%s

Incident Description:
%s

Response format exactly:

Probable Issue:
<write here>

Debugging Suggestions:
<write here>

Possible Fixes:
<write here>
"""
                .formatted(title, description);

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