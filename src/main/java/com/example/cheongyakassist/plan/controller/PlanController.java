package com.example.cheongyakassist.plan.controller;

import com.example.cheongyakassist.plan.dto.PlanCreateRequest;
import com.example.cheongyakassist.plan.dto.PlanResponseDto;
import com.example.cheongyakassist.plan.service.PlanService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    /**
     * LLM 결과 저장
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PlanCreateResponse>> createPlan(
            @Valid @RequestBody PlanCreateRequest request
    ) {
        Long planId = planService.createPlan(request);
        PlanCreateResponse body = new PlanCreateResponse(planId, request.getSurveyId());
        return ResponseEntity.ok(ApiResponse.success("AI 설계 결과가 저장되었습니다.", body));
    }

    /**
     * 특정 설문에 대한 최신 Plan 조회
     * GET /api/plans/{surveyId}
     */
    @GetMapping("/{surveyId}")
    public ResponseEntity<ApiResponse<PlanResponseDto>> getPlanBySurveyId(
            @PathVariable Long surveyId
    ) {
        PlanResponseDto plan = planService.getLatestPlanBySurveyId(surveyId);
        return ResponseEntity.ok(ApiResponse.success("OK", plan));
    }

    // --- 내부 응답 DTO ---

    @Getter
    public static class PlanCreateResponse {
        private final Long planId;
        private final Long surveyId;

        public PlanCreateResponse(Long planId, Long surveyId) {
            this.planId = planId;
            this.surveyId = surveyId;
        }
    }

    // --- 공통 ApiResponse (SurveyController와 구조 동일) ---

    @Getter
    public static class ApiResponse<T> {
        private final boolean success;
        private final String message;
        private final T data;

        private ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public static <T> ApiResponse<T> success(String message, T data) {
            return new ApiResponse<>(true, message, data);
        }

        public static <T> ApiResponse<T> failure(String message) {
            return new ApiResponse<>(false, message, null);
        }
    }
}
