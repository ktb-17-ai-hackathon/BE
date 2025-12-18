package com.example.cheongyakassist.plan.dto;

import com.example.cheongyakassist.plan.model.ConfidenceLevel;
import com.example.cheongyakassist.plan.model.PlanHorizon;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PlanResponseDto {

    private Long planId;
    private Long surveyId;

    // LLM 결과 전체(JSON)
    private JsonNode llmRawResult;

    private PlanHorizon recommendedHorizon;
    private ConfidenceLevel confidenceLevel;

    private LocalDateTime createdAt;
}
