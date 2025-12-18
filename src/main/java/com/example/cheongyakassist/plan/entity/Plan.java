package com.example.cheongyakassist.plan.entity;

import com.example.cheongyakassist.plan.model.ConfidenceLevel;
import com.example.cheongyakassist.plan.model.PlanHorizon;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "plan")
@Getter
@NoArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 설문(survey)에 대한 플랜인지 연결
    @Column(name = "survey_id", nullable = false)
    private Long surveyId;

    // LLM이 준 JSON 전체 (문자열로 저장)
    @Lob
    @Column(name = "llm_result_json", nullable = false)
    private String llmResultJson;

    // 선택: 추천 기간 (3/5/10년 등)
    @Enumerated(EnumType.STRING)
    @Column(name = "recommended_horizon")
    private PlanHorizon recommendedHorizon;

    // 선택: 신뢰도
    @Enumerated(EnumType.STRING)
    @Column(name = "confidence_level")
    private ConfidenceLevel confidenceLevel;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public static Plan create(
            Long surveyId,
            String llmResultJson,
            PlanHorizon recommendedHorizon,
            ConfidenceLevel confidenceLevel
    ) {
        Plan plan = new Plan();
        plan.surveyId = surveyId;
        plan.llmResultJson = llmResultJson;
        plan.recommendedHorizon = recommendedHorizon;
        plan.confidenceLevel = confidenceLevel;
        return plan;
    }
}
