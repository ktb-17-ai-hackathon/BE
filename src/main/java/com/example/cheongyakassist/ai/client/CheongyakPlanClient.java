package com.example.cheongyakassist.ai.client;

import com.example.cheongyakassist.ai.dto.AiCheongyakPlanResponse;
import com.example.cheongyakassist.survey.entity.HousingProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheongyakPlanClient {

    private final RestTemplate restTemplate;

    @Value("${ai.cheongyak-plan.url}")
    private String aiUrl;

    public AiCheongyakPlanResponse requestPlan(HousingProfile profile) {
        log.info("═══════════════════════════════════════════");
        log.info("🚀 STARTING FastAPI REQUEST");
        log.info("URL: {}", aiUrl);
        log.info("═══════════════════════════════════════════");

        // FastAPI 요청 바디 구성
        Map<String, Object> body = buildRequestBody(profile);

        log.info("📤 Request body: {}", body);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            log.info("⏳ Calling RestTemplate.exchange()...");

            ResponseEntity<AiCheongyakPlanResponse> resp = restTemplate.exchange(
                    aiUrl,
                    HttpMethod.POST,
                    entity,
                    AiCheongyakPlanResponse.class
            );

            log.info("✅ SUCCESS! Status: {}", resp.getStatusCode());
            log.info("Response: {}", resp.getBody());

            return resp.getBody();

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("═══════════════════════════════════════════");
            log.error("❌ HTTP ERROR from FastAPI");
            log.error("Status: {}", e.getStatusCode());
            log.error("Response body: {}", e.getResponseBodyAsString());
            log.error("═══════════════════════════════════════════", e);

            return createMockResponse(profile);

        } catch (org.springframework.web.client.ResourceAccessException e) {
            log.error("═══════════════════════════════════════════");
            log.error("❌ CONNECTION FAILED");
            log.error("Message: {}", e.getMessage());
            log.error("Cause: {}", e.getCause() != null ? e.getCause().getMessage() : "N/A");
            log.error("═══════════════════════════════════════════", e);

            return createMockResponse(profile);

        } catch (Exception e) {
            log.error("═══════════════════════════════════════════");
            log.error("❌ UNEXPECTED ERROR");
            log.error("Type: {}", e.getClass().getName());
            log.error("Message: {}", e.getMessage());
            log.error("═══════════════════════════════════════════", e);

            return createMockResponse(profile);
        }
    }

    private Map<String, Object> buildRequestBody(HousingProfile profile) {
        Map<String, Object> body = new HashMap<>();

        body.put("surveyId", profile.getId());
        body.put("age", profile.getAge());
        body.put("marryStatus", profile.getMarryStatus());
        body.put("fMarryStatus", profile.getFMarryStatus());

        body.put("childCount", profile.getChildCount() != null ? profile.getChildCount() : 0);
        body.put("fChildCount", profile.getFChildCount());
        body.put("isDoubleIncome", profile.getIsDoubleIncome());
        body.put("fIsDoubleIncome", profile.getFIsDoubleIncome());
        body.put("willContinueDoubleIncome", profile.getWillContinueDoubleIncome());

        body.put("currentDistrict", profile.getCurrentDistrict() != null ? profile.getCurrentDistrict() : "");
        body.put("isHouseholder", profile.getIsHouseholder() != null ? profile.getIsHouseholder() : false);
        body.put("hasOwnedHouse", profile.getHasOwnedHouse() != null ? profile.getHasOwnedHouse() : false);
        body.put("unhousedStartYear", profile.getUnhousedStartYear());
        body.put("isSupportingParents", profile.getIsSupportingParents() != null ? profile.getIsSupportingParents() : false);
        body.put("fIsSupportingParents", profile.getFIsSupportingParents());

        body.put("jobTitle", profile.getJobTitle() != null ? profile.getJobTitle() : "");
        body.put("jobDistrict", profile.getJobDistrict() != null ? profile.getJobDistrict() : "");
        body.put("annualIncome", profile.getAnnualIncome() != null ? profile.getAnnualIncome() : 0);
        body.put("annualSideIncome", profile.getAnnualSideIncome() != null ? profile.getAnnualSideIncome() : 0);
        body.put("monthlySavingAmount", profile.getMonthlySavingAmount() != null ? profile.getMonthlySavingAmount() : 0);
        body.put("currentFinancialAssets", profile.getCurrentFinancialAssets() != null ? profile.getCurrentFinancialAssets() : 0);
        body.put("additionalAssets", profile.getAdditionalAssets() != null ? profile.getAdditionalAssets() : 0);
        body.put("targetSavingRate", profile.getTargetSavingRate());

        body.put("hasDebt", profile.getHasDebt() != null ? profile.getHasDebt() : false);
        body.put("debtType", profile.getDebtType() != null ? profile.getDebtType() : "none");
        body.put("debtPrincipal", profile.getDebtPrincipal());
        body.put("debtInterestRateBand", profile.getDebtInterestRateBand() != null ? profile.getDebtInterestRateBand() : "UNKNOWN");
        body.put("debtPrincipalPaid", profile.getDebtPrincipalPaid() != null ? profile.getDebtPrincipalPaid() : 0);
        body.put("monthlyDebtPayment", profile.getMonthlyDebtPayment() != null ? profile.getMonthlyDebtPayment() : 0);

        body.put("hasSubscriptionAccount", profile.getHasSubscriptionAccount() != null ? profile.getHasSubscriptionAccount() : false);
        body.put("subscriptionStartDate", formatDate(profile.getSubscriptionStartDate()));
        body.put("fSubscriptionStartDate", formatDate(profile.getFSubscriptionStartDate()));
        body.put("monthlySubscriptionAmount", profile.getMonthlySubscriptionAmount() != null ? profile.getMonthlySubscriptionAmount() : 0);
        body.put("totalSubscriptionBalance", profile.getTotalSubscriptionBalance() != null ? profile.getTotalSubscriptionBalance() : 0);

        body.put("targetSubscriptionType", profile.getTargetSubscriptionType() != null ? profile.getTargetSubscriptionType() : "both");
        body.put("preferredRegion", profile.getPreferredRegion() != null ? profile.getPreferredRegion() : "");
        body.put("priorityCriteria", parsePriorityCriteria(profile.getPriorityCriteria()));
        body.put("preferredHousingSize", profile.getPreferredHousingSize() != null ? profile.getPreferredHousingSize() : "");

        return body;
    }

    private String formatDate(LocalDate date) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private List<String> parsePriorityCriteria(String priorityCriteria) {
        if (priorityCriteria == null || priorityCriteria.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(priorityCriteria.split(","));
    }

    private AiCheongyakPlanResponse createMockResponse(HousingProfile profile) {
        log.warn("⚠️ Returning MOCK data");

        AiCheongyakPlanResponse response = new AiCheongyakPlanResponse();

        AiCheongyakPlanResponse.Summary summary = new AiCheongyakPlanResponse.Summary();
        summary.setTitle("5년 안에 수도권 아파트 도전이 가능합니다.");
        summary.setBody("현재 연 소득과 자산, 저축 여력을 고려했을 때 5년 안에 실거주용 아파트 청약을 목표로 하는 전략이 유효합니다.");
        response.setSummary(summary);

        AiCheongyakPlanResponse.Diagnosis diagnosis = new AiCheongyakPlanResponse.Diagnosis();
        diagnosis.setCanBuyWithCheongyak(true);
        diagnosis.setConfidenceLevel("MEDIUM");
        diagnosis.setReasons(Arrays.asList(
                "무주택 + 청약 통장 보유로 기본 자격 충족",
                "현재 소득 대비 저축 여력 양호",
                "5년간 자산 증가 시뮬레이션 결과 긍정적"
        ));
        response.setDiagnosis(diagnosis);

        AiCheongyakPlanResponse.TimeHorizonStrategy strategy = new AiCheongyakPlanResponse.TimeHorizonStrategy();
        strategy.setNow("지금은 청약 통장 납입액을 최소 기준 이상으로 맞추고, 부채 비율을 관리하는 시기입니다.");
        strategy.setThreeYears("3년 차에는 청약 가점, 무주택 기간, 소득 요건을 다시 점검하고, 직장/생활권에 맞는 후보 지역을 2~3곳으로 압축하세요.");
        strategy.setFiveYears("5년 차에는 실제 청약 일정과 분양 공고를 캘린더로 관리하면서, 계약금/중도금 마련 플랜을 구체화하는 단계입니다.");
        response.setTimeHorizonStrategy(strategy);

        AiCheongyakPlanResponse.ChartData chartData = new AiCheongyakPlanResponse.ChartData();
        List<AiCheongyakPlanResponse.SavingProjection> projections = new ArrayList<>();
        int baseAmount = profile.getCurrentFinancialAssets() != null ? profile.getCurrentFinancialAssets() : 80000000;
        for (int i = 0; i <= 5; i++) {
            AiCheongyakPlanResponse.SavingProjection proj = new AiCheongyakPlanResponse.SavingProjection();
            proj.setYear(i);
            proj.setAmount(baseAmount + (i * 25000000));
            projections.add(proj);
        }
        chartData.setSavingProjectionByYear(projections);
        response.setChartData(chartData);

        AiCheongyakPlanResponse.PlanMeta planMeta = new AiCheongyakPlanResponse.PlanMeta();
        planMeta.setRecommendedHorizon("MID_5");
        planMeta.setReason("5년 차에 가용 예산이 목표치에 도달하는 구간으로 추정됩니다.");
        response.setPlanMeta(planMeta);

        return response;
    }
}
