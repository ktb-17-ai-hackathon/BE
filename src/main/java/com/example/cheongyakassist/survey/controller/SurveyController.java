package com.example.cheongyakassist.survey.controller;

import com.example.cheongyakassist.survey.dto.SurveyCreateRequest;
import com.example.cheongyakassist.survey.dto.SurveyResponseDto;
import com.example.cheongyakassist.survey.service.SurveyService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<ApiResponse<SurveyCreateResponse>> createSurvey(
            @Valid @RequestBody SurveyCreateRequest request
    ) {
        Long surveyId = surveyService.createSurvey(request);
        SurveyCreateResponse body = new SurveyCreateResponse(surveyId);
        return ResponseEntity.ok(ApiResponse.success("설문이 저장되었습니다.", body));
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<ApiResponse<SurveyResponseDto>> getSurvey(
            @PathVariable Long surveyId
    ) {
        SurveyResponseDto survey = surveyService.getSurvey(surveyId);
        return ResponseEntity.ok(ApiResponse.success("OK", survey));
    }

    // --- 내부 Response DTO ---

    @Getter
    public static class SurveyCreateResponse {
        private final Long surveyId;

        public SurveyCreateResponse(Long surveyId) {
            this.surveyId = surveyId;
        }
    }

    // --- 공통 ApiResponse 래퍼 (프로젝트에 이미 있으면 그걸로 교체) ---

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
