package com.example.cheongyakassist.plan.repository;

import com.example.cheongyakassist.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    // survey 연관관계를 통한 조회
    Optional<Plan> findTopBySurvey_IdOrderByCreatedAtDesc(Long surveyId);
}