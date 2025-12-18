package com.example.cheongyakassist.plan.repository;

import com.example.cheongyakassist.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    // 하나의 설문에 여러 번 Plan을 만들 수 있다고 가정하면
    // 가장 최근 Plan 1개 조회
    Optional<Plan> findTopBySurveyIdOrderByCreatedAtDesc(Long surveyId);
}
