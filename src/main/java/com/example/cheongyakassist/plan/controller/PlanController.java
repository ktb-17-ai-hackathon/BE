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


    /**
     * 플랜 생성
     * POST /api/plans
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CreatePlanResponse>> createPlan(
            @RequestBody PlanCreateRequest request
    ) {
        Long planId = planService.createPlan(request);
        return ResponseEntity.ok(
                ApiResponse.success("플랜이 생성되었습니다.", new CreatePlanResponse(planId))
        );
    }

    /**
     * 특정 설문에 대한 최신 플랜 조회
     * GET /api/plans/survey/{surveyId}
     */
    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<ApiResponse<PlanResponseDto>> getLatestPlanBySurveyId(
            @PathVariable Long surveyId
    ) {
        PlanResponseDto dto = planService.getLatestPlanBySurveyId(surveyId);
        return ResponseEntity.ok(ApiResponse.success("OK", dto));
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
