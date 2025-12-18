// src/main/java/com/example/cheongyakassist/plan/dto/PlanCreateRequest.java
package com.example.cheongyakassist.plan.dto;

import com.example.cheongyakassist.plan.model.ConfidenceLevel;
import com.example.cheongyakassist.plan.model.PlanHorizon;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanCreateRequest {

    // 어떤 설문(survey)의 결과인지
    private Long surveyId;

    // LLM이 준 원본 JSON (프론트나 AI 서버에서 만들어서 넘겨줌)
    private JsonNode llmRawResult;

    // 추천 기간 (3/5/10년 등)
    private PlanHorizon recommendedHorizon;

    // 신뢰도
    private ConfidenceLevel confidenceLevel;
}
