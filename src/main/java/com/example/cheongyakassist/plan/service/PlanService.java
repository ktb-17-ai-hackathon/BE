package com.example.cheongyakassist.plan.service;

import com.example.cheongyakassist.ai.client.CheongyakPlanClient;
import com.example.cheongyakassist.ai.dto.AiCheongyakPlanResponse;
import com.example.cheongyakassist.plan.dto.PlanCreateRequest;
import com.example.cheongyakassist.plan.dto.PlanResponseDto;
import com.example.cheongyakassist.plan.entity.Plan;
import com.example.cheongyakassist.plan.repository.PlanRepository;
import com.example.cheongyakassist.survey.entity.HousingProfile;
import com.example.cheongyakassist.survey.repository.HousingProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanService {

    private final PlanRepository planRepository;
    private final HousingProfileRepository housingProfileRepository;
    private final CheongyakPlanClient cheongyakPlanClient;
    private final ObjectMapper objectMapper;

    /**
     * 프론트에서 surveyId + LLM 결과를 모두 넘겨주는 경우
     */
    @Transactional
    public Long createPlan(PlanCreateRequest request) {
        log.info("[PlanService] createPlan request = {}", request);

        HousingProfile survey = housingProfileRepository.findById(request.getSurveyId())
                .orElseThrow(() -> new IllegalArgumentException("Survey not found: " + request.getSurveyId()));

        Plan plan = Plan.builder()
                .survey(survey)
                .recommendedHorizon(request.getRecommendedHorizon() != null
                        ? request.getRecommendedHorizon().name()
                        : null)
                .confidenceLevel(request.getConfidenceLevel() != null
                        ? request.getConfidenceLevel().name()
                        : null)
                .llmRawResult(convertToJsonString(request.getLlmRawResult()))
                .createdAt(LocalDateTime.now())
                .build();

        Plan saved = planRepository.save(plan);
        log.info("[PlanService] createPlan saved id = {}", saved.getId());
        return saved.getId();
    }

    @Transactional
    public Long createPlanFromSurvey(Long surveyId) {
        PlanResponseDto dto = createPlanWithAi(surveyId);
        return dto.getPlanId();
    }

    @Transactional
    public PlanResponseDto createPlanWithAi(Long surveyId) {
        log.info("[PlanService] createPlanWithAi surveyId = {}", surveyId);

        HousingProfile survey = housingProfileRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found: " + surveyId));

        // FastAPI 호출
        AiCheongyakPlanResponse aiRes = cheongyakPlanClient.requestPlan(survey);
        log.info("[PlanService] AI response = {}", aiRes);

        // Plan 엔티티 생성/저장
        Plan plan = Plan.builder()
                .survey(survey)
                // ✅ planMeta에서 가져오기
                .recommendedHorizon(aiRes.getPlanMeta() != null
                        ? aiRes.getPlanMeta().getRecommendedHorizon()
                        : null)
                // ✅ diagnosis에서 가져오기
                .confidenceLevel(aiRes.getDiagnosis() != null
                        ? aiRes.getDiagnosis().getConfidenceLevel()
                        : null)
                // ✅ 전체 응답 객체를 JSON 문자열로 변환
                .llmRawResult(convertToJsonString(aiRes))
                .createdAt(LocalDateTime.now())
                .build();

        Plan saved = planRepository.save(plan);
        log.info("[PlanService] createPlanWithAi saved id = {}", saved.getId());

        return PlanResponseDto.from(saved);
    }

    @Transactional(readOnly = true)
    public PlanResponseDto getLatestPlanBySurveyId(Long surveyId) {
        Plan plan = planRepository.findTopBySurvey_IdOrderByCreatedAtDesc(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found for surveyId = " + surveyId));

        return PlanResponseDto.from(plan);
    }

    /**
     * Object를 JSON String으로 변환
     */
    private String convertToJsonString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Failed to convert to JSON string", e);
            throw new RuntimeException("Failed to convert to JSON string", e);
        }
    }
}