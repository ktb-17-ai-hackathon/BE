package com.example.cheongyakassist.survey.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class SurveyResponseDto {

    private Long surveyId;

    // ========== 1. 기본 정보 ==========
    private Integer age;
    private String marryStatus;       // single / married / divorced_or_widowed
    private Boolean fMarryStatus;     // 미래 결혼 계획

    // ========== 2. 가족 & 자녀 계획 ==========
    private Integer childCount;
    private Integer fChildCount;
    private Boolean isDoubleIncome;
    private Boolean fIsDoubleIncome;
    private Boolean willContinueDoubleIncome;

    // ========== 3. 현재 집/부모 관련 ==========
    private String currentDistrict;
    private Boolean isHouseholder;
    private Boolean hasOwnedHouse;
    private Integer unhousedStartYear;
    private Boolean isSupportingParents;
    private Boolean fIsSupportingParents;

    // ========== 4. 돈 흐름 (소득/자산/부채) ==========
    private String jobTitle;
    private String jobDistrict;

    private Integer annualIncome;
    private Integer annualSideIncome;
    private Integer monthlySavingAmount;
    private Integer currentFinancialAssets;
    private Integer additionalAssets;
    private Integer targetSavingRate;

    private Boolean hasDebt;
    private String debtType;
    private Integer debtPrincipal;
    private String debtInterestRateBand;
    private Integer debtPrincipalPaid;
    private Integer monthlyDebtPayment;

    // ========== 5. 청약 준비 상태 ==========
    private Boolean hasSubscriptionAccount;
    private LocalDate subscriptionStartDate;
    private LocalDate fSubscriptionStartDate;
    private Integer monthlySubscriptionAmount;
    private Integer totalSubscriptionBalance;

    // ========== 6. 살고 싶은 집/선호 ==========
    private String targetSubscriptionType;
    private String preferredRegion;

    /**
     * priorityCriteria 는 엔티티에서는
     * "transport,school,price" 처럼 콤마 구분 문자열로 저장
     */
    private String priorityCriteria;

    private String preferredHousingSize;

    // 공통
    private LocalDateTime createdAt;
}
