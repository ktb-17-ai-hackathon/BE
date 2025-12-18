package com.example.cheongyakassist.plan.dto;

import com.example.cheongyakassist.plan.model.ConfidenceLevel;
import com.example.cheongyakassist.plan.model.PlanHorizon;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanCreateRequest {

    @NotNull
    private Long surveyId;

    /**
     * LLM이 반환한 JSON 전체
     * 예: summary/diagnosis/timeHorizonStrategy/actionItems/chartData...
     */
    @NotNull
    private JsonNode llmRawResult;

    /**
     * 선택: LLM이 추천한 기간 (3년/5년/10년 등)
     * 없으면 null로 보내도 됨
     */
    private PlanHorizon recommendedHorizon;

    /**
     * 선택: LLM이 제공한 신뢰도 (LOW/MEDIUM/HIGH 등)
     */
    private ConfidenceLevel confidenceLevel;
}
