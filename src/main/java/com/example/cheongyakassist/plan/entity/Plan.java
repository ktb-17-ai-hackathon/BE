package com.example.cheongyakassist.plan.entity;

import com.example.cheongyakassist.survey.entity.HousingProfile;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "plan")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // HousingProfile과 연관관계 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private HousingProfile survey;

    // 추천 보유 기간 (SHORT_3, MID_5, LONG_10)
    @Column(name = "recommended_horizon", length = 20)
    private String recommendedHorizon;

    // 신뢰도 (LOW, MEDIUM, HIGH)
    @Column(name = "confidence_level", length = 20)
    private String confidenceLevel;

    // LLM 원본 JSON - String으로 저장
    @Lob
    @Column(name = "llm_raw_result", columnDefinition = "CLOB")
    private String llmRawResult;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}