package com.example.cheongyakassist.survey.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SurveyCreateRequest {

    // ========== 1. 기본 정보 ==========
    /** 만 나이 (필수: 프론트에서 유효성 검사로 강제) */
    private Integer age;

    /** 결혼 상태: single / married / divorced_or_widowed */
    private String marryStatus;

    /** (미혼일 때) 앞으로 결혼할 계획 여부 */
    private Boolean fMarryStatus;

    // ========== 2. 가족 & 자녀 계획 ==========

    /** 현재 자녀 수 (없으면 0) */
    private Integer childCount;

    /** 앞으로 추가로 낳을 자녀 수 (없으면 0) */
    private Integer fChildCount;

    /**
     * 현재 맞벌이 여부 (지금 상태 기준)
     * true: 맞벌이, false: 외벌이, null: 잘 모르겠어요
     */
    private Boolean isDoubleIncome;

    /**
     * 미래 맞벌이 계획 (결혼/추가 자녀 등 미래 기준)
     * true: 맞벌이 예정, false: 외벌이 예정, null: 잘 모르겠어요
     */
    private Boolean fIsDoubleIncome;

    /** (자녀 계획이 있을 때) 아이를 낳은 뒤에도 맞벌이 유지 계획 */
    private Boolean willContinueDoubleIncome;

    // ========== 3. 현재 집/부모 관련 ==========
    /** 현재 거주 구 (ex: "송파구", "하남시") */
    private String currentDistrict;

    /**
     * 집 대표 여부 (세대주인지)
     * true: 본인이 세대주, false: 부모/배우자가 세대주, null: 잘 모름
     */
    private Boolean isHouseholder;

    /**
     * 지금까지 본인 명의로 집 소유 이력
     * true: 예전에/지금 집을 가진 적 있음
     * false: 한 번도 없음
     * null: (모름 처리할 경우)
     */
    private Boolean hasOwnedHouse;

    /**
     * 무주택 시작 연도 (예전에 집이 있었고, 처분 후 무주택 기간 계산용)
     * ex) 2019
     */
    private Integer unhousedStartYear;

    /** 현재 만 60세 이상 부모님을 3년 이상 부양 중인지 */
    private Boolean isSupportingParents;

    /** 미래에 부모님을 부양하거나 함께 살 계획인지 */
    private Boolean fIsSupportingParents;


    // ========== 4. 돈 흐름 (소득/자산/부채) ==========

    /** 현재 직업/직무 (자유 입력) */
    private String jobTitle;

    /** 현재 또는 희망하는 직장 위치(구 단위) */
    private String jobDistrict;

    /** 연 소득 (주수입, 없으면 0) */
    private Integer annualIncome;

    /** 연 부수입 (없으면 0) */
    private Integer annualSideIncome;

    /** 월 저축 금액 (없으면 0) */
    private Integer monthlySavingAmount;

    /** 현재까지 모아둔 현금/예금 총액 */
    private Integer currentFinancialAssets;

    /**
     * 추가로 활용 가능한 자산 총합
     * (주식/코인 + 자동차 + 부모님 도움 + 기타 합산)
     */
    private Integer additionalAssets;

    /**
     * 향후 월급 대비 목표 저축률 (%)
     * ex) 30 (30%)
     */
    private Integer targetSavingRate;

    // ---- 부채 관련 (질문 9~13) ----

    /** 현재 대출/빚 존재 여부 */
    private Boolean hasDebt;

    /**
     * 대출 유형 (주택 / 학자금 / 신용 / mixed 등)
     * ex) "housing", "student", "credit", "mixed", "none"
     */
    private String debtType;

    /** 남아 있는 대출 원금 총액 (없으면 0) */
    private Integer debtPrincipal;

    /**
     * 평균 이자율 밴드
     * ex) "LT_2", "BETWEEN_2_4", "BETWEEN_4_6", "GT_6", "UNKNOWN"
     */
    private String debtInterestRateBand;

    /** 지금까지 상환한 원금 총액 (없으면 0) */
    private Integer debtPrincipalPaid;

    /** 매달 대출 상환으로 나가는 금액 (없으면 0) */
    private Integer monthlyDebtPayment;


    // ========== 5. 청약 준비 상태 ==========

    /** 청약 통장 보유 여부 */
    private Boolean hasSubscriptionAccount;

    /** 실제 청약 통장 납입 시작 시점 (연/월) */
    private LocalDate subscriptionStartDate;

    /** 미래에 청약 통장을 만들 계획이 있다면 그 시점 (연/월) */
    private LocalDate fSubscriptionStartDate;

    /** 청약 통장 월 납입액 (없으면 0) */
    private Integer monthlySubscriptionAmount;

    /** 현재까지 청약 통장에 모인 잔액 */
    private Integer totalSubscriptionBalance;


    // ========== 6. 살고 싶은 집/선호 ==========

    /**
     * 청약 유형
     * "public" (공공분양), "private" (민영분양), "both"
     */
    private String targetSubscriptionType;

    /**
     * 선호 지역 (광역/지방 등으로 도메인 나눌지, 그냥 구 이름으로 갈지 결정 필요)
     * 일단 문자열로 두고, 나중에 코드화 가능
     */
    private String preferredRegion;

    /**
     * 가장 중요한 요소들 (복수 선택)
     * 예: ["transport", "school", "price"]
     */
    private List<String> priorityCriteria;

    /**
     * 선호하는 집 크기 (선택지 내용을 그대로 저장)
     * ex) "20평대 초반", "25평 내외", "34평 내외", ...
     */
    private String preferredHousingSize;
}
