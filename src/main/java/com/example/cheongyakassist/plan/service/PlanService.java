// src/main/java/com/example/cheongyakassist/plan/service/PlanService.java
package com.example.cheongyakassist.plan.service;

import com.example.cheongyakassist.plan.dto.PlanCreateRequest;
import com.example.cheongyakassist.plan.dto.PlanResponseDto;
import com.example.cheongyakassist.plan.entity.Plan;
import com.example.cheongyakassist.plan.repository.PlanRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Long createPlan(PlanCreateRequest request) {
        String llmResultJson = toJsonString(request.getLlmRawResult());

        Plan plan = Plan.create(
                request.getSurveyId(),
                llmResultJson,
                request.getRecommendedHorizon(),
                request.getConfidenceLevel()
        );

        Plan saved = planRepository.save(plan);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public PlanResponseDto getLatestPlanBySurveyId(Long surveyId) {
        Plan plan = planRepository.findTopBySurveyIdOrderByCreatedAtDesc(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 설문에 대한 플랜이 없습니다. surveyId=" + surveyId));

        JsonNode llmRawResult = toJsonNode(plan.getLlmResultJson());

        return PlanResponseDto.builder()
                .planId(plan.getId())
                .surveyId(plan.getSurveyId())
                .llmRawResult(llmRawResult)
                .recommendedHorizon(plan.getRecommendedHorizon())
                .confidenceLevel(plan.getConfidenceLevel())
                .createdAt(plan.getCreatedAt())
                .build();
    }

    private String toJsonString(JsonNode node) {
        try {
            return objectMapper.writeValueAsString(node);
        } catch (Exception e) {
            throw new IllegalStateException("LLM 결과 JSON 직렬화 중 오류가 발생했습니다.", e);
        }
    }

    private JsonNode toJsonNode(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            throw new IllegalStateException("LLM 결과 JSON 역직렬화 중 오류가 발생했습니다.", e);
        }
    }
}
