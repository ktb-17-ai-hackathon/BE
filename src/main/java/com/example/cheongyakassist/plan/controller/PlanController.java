// src/main/java/com/example/cheongyakassist/plan/controller/PlanController.java
package com.example.cheongyakassist.plan.controller;

import com.example.cheongyakassist.plan.dto.PlanCreateRequest;
import com.example.cheongyakassist.plan.dto.PlanResponseDto;
import com.example.cheongyakassist.plan.service.PlanService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping("/test")
    public String test() {
        return "plan-ok";
    }

    // (기존) 프론트가 직접 LLM 결과까지 주는 경우
    @PostMapping
    public ResponseEntity<ApiResponse<CreatePlanResponse>> createPlan(
            @RequestBody PlanCreateRequest request
    ) {
        Long planId = planService.createPlan(request);
        return ResponseEntity.ok(
                ApiResponse.success("플랜이 생성되었습니다.", new CreatePlanResponse(planId))
        );
    }

    // (신규) 설문 ID만 보내면 백엔드가 AI 호출까지 해서 Plan 생성
    @PostMapping("/survey/{surveyId}/ai")
    public ResponseEntity<ApiResponse<CreatePlanResponse>> createPlanFromSurvey(
            @PathVariable Long surveyId
    ) {
        Long planId = planService.createPlanFromSurvey(surveyId);
        return ResponseEntity.ok(
                ApiResponse.success("AI 설계 결과가 생성되었습니다.", new CreatePlanResponse(planId))
        );
    }

    // 최신 Plan 조회
    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<ApiResponse<PlanResponseDto>> getLatestPlanBySurveyId(
            @PathVariable Long surveyId
    ) {
        PlanResponseDto dto = planService.getLatestPlanBySurveyId(surveyId);
        return ResponseEntity.ok(ApiResponse.success("OK", dto));
    }

    @PostMapping("/ai/{surveyId}")
    public ApiResponse<PlanResponseDto> createPlanWithAi(
            @PathVariable Long surveyId
    ) {
        PlanResponseDto dto = planService.createPlanWithAi(surveyId);
        return ApiResponse.success("플랜이 생성되었습니다.", dto);
    }

    // ---------- 내부 DTO들 ----------

    @Getter
    public static class CreatePlanResponse {
        private final Long planId;

        public CreatePlanResponse(Long planId) {
            this.planId = planId;
        }
    }

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
