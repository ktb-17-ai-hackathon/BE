package com.example.cheongyakassist.plan.dto;

import com.example.cheongyakassist.plan.entity.Plan;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanResponseDto {

    private Long planId;
    private Long surveyId;
    private String recommendedHorizon;
    private String confidenceLevel;
    private JsonNode llmRawResult; // 프론트로는 JsonNode로 전달
    private LocalDateTime createdAt;

    public static PlanResponseDto from(Plan plan) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode llmJson = null;

        if (plan.getLlmRawResult() != null) {
            try {
                llmJson = mapper.readTree(plan.getLlmRawResult());
            } catch (JsonProcessingException e) {
                // JSON 파싱 실패 시 null로 처리
            }
        }

        return PlanResponseDto.builder()
                .planId(plan.getId())
                .surveyId(plan.getSurvey().getId())
                .recommendedHorizon(plan.getRecommendedHorizon())
                .confidenceLevel(plan.getConfidenceLevel())
                .llmRawResult(llmJson)
                .createdAt(plan.getCreatedAt())
                .build();
    }
}