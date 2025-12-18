package com.example.cheongyakassist.survey.entity;

import com.example.cheongyakassist.survey.dto.SurveyCreateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "housing_profile")
@Getter
@NoArgsConstructor
public class HousingProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================== 1. 기본 정보 ==================

    /** 만 나이 */
    private Integer age;

    /** 결혼 상태: single / married / divorced_or_widowed */
    @Column(length = 20)
    private String marryStatus;

    /** (미혼일 때) 앞으로 결혼할 계획 여부 */
    private Boolean fMarryStatus;


    // ================== 2. 가족 & 자녀 계획 ==================

    /** 현재 자녀 수 (없으면 0 가능) */
    private Integer childCount;

    /** 앞으로 추가로 낳을 자녀 수 (없으면 0 가능) */
    private Integer fChildCount;

    /**
     * 현재 맞벌이 여부
     * true: 맞벌이, false: 외벌이, null: 잘 모름
     */
    private Boolean isDoubleIncome;

    /**
     * 미래 맞벌이 계획
     * true: 맞벌이 예정, false: 외벌이 예정, null: 잘 모름
     */
    private Boolean fIsDoubleIncome;

    /** (자녀 계획이 있다면) 아이 낳은 뒤에도 맞벌이 유지 계획 */
    private Boolean willContinueDoubleIncome;


    // ================== 3. 현재 집/부모 관련 ==================

    /** 현재 거주 구 (예: 송파구, 하남시) */
    @Column(length = 50)
    private String currentDistrict;

    /**
     * 세대주 여부
     * true: 본인이 세대주, false: 부모/배우자 세대주, null: 잘 모름
     */
    private Boolean isHouseholder;

    /**
     * 집 소유 이력
     * true: 예전에/지금 집을 가진 적 있음
     * false: 한 번도 없음
     * null: 모름
     */
    private Boolean hasOwnedHouse;

    /**
     * 무주택 시작 연도 (예전에 집을 처분했다면 그 이후 연도)
     * 예: 2019
     */
    private Integer unhousedStartYear;

    /** 현재 만 60세 이상 부모님을 3년 이상 부양 중인지 */
    private Boolean isSupportingParents;

    /** 미래에 부모님을 부양하거나 함께 살 계획인지 */
    private Boolean fIsSupportingParents;


    // ================== 4. 돈 흐름 (소득/자산/부채) ==================

    /** 현재 직업/직무 (자유 입력) */
    @Column(length = 50)
    private String jobTitle;

    /** 현재 또는 희망 직장 위치(구 단위) */
    @Column(length = 50)
    private String jobDistrict;

    /** 연 소득 (주수입, 없으면 0 가능) */
    private Integer annualIncome;

    /** 연 부수입 (없으면 0 가능) */
    private Integer annualSideIncome;

    /** 월 저축 금액 (없으면 0 가능) */
    private Integer monthlySavingAmount;

    /** 현재까지 모아둔 현금/예금 총액 */
    private Integer currentFinancialAssets;

    /** 추가로 활용 가능한 자산 총합 (주식/코인/자동차/부모님 도움 등 합산) */
    private Integer additionalAssets;

    /** 향후 월급 대비 목표 저축률 (%) */
    private Integer targetSavingRate;

    // ---- 부채 관련 ----

    /** 현재 대출/빚 존재 여부 */
    private Boolean hasDebt;

    /**
     * 대출 유형
     * 예: housing, student, credit, mixed, none 등
     */
    @Column(length = 20)
    private String debtType;

    /** 남아 있는 대출 원금 총액 */
    private Integer debtPrincipal;

    /**
     * 평균 이자율 밴드
     * 예: LT_2 / BETWEEN_2_4 / BETWEEN_4_6 / GT_6 / UNKNOWN
     */
    @Column(length = 20)
    private String debtInterestRateBand;

    /** 지금까지 상환한 원금 총액 */
    private Integer debtPrincipalPaid;

    /** 매달 대출 상환 금액 */
    private Integer monthlyDebtPayment;


    // ================== 5. 청약 준비 상태 ==================

    /** 청약 통장 보유 여부 */
    private Boolean hasSubscriptionAccount;

    /** 실제 청약 통장 납입 시작 시점 (연/월 기준, 일자는 1일로 처리 가능) */
    private LocalDate subscriptionStartDate;

    /** 미래 청약 통장 개설 계획 시점 */
    private LocalDate fSubscriptionStartDate;

    /** 청약 통장 월 납입액 */
    private Integer monthlySubscriptionAmount;

    /** 현재까지 청약 통장에 모인 잔액 */
    private Integer totalSubscriptionBalance;


    // ================== 6. 살고 싶은 집/선호 ==================

    /**
     * 청약 유형
     * public (공공분양) / private (민영분양) / both
     */
    @Column(length = 10)
    private String targetSubscriptionType;

    /** 선호 지역 (메트로/지방 or 구 이름 등) */
    @Column(length = 50)
    private String preferredRegion;

    /**
     * 집을 고를 때 우선순위 요소들
     * 예: "transport,school,price" 처럼 콤마 구분 문자열로 저장
     * (프론트에서 배열 <-> 문자열 변환)
     */
    @Column(length = 255)
    private String priorityCriteria;

    /**
     * 선호하는 집 크기
     * 예: "20평대 초반", "25평 내외", "34평 내외" 등 선택지 텍스트 그대로
     */
    @Column(length = 50)
    private String preferredHousingSize;


    // ================== 시스템 공통 ==================

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // =============== 정적 팩토리 메서드 ===============

    public static HousingProfile from(SurveyCreateRequest req) {
        HousingProfile p = new HousingProfile();

        // 1. 기본 정보
        p.age = req.getAge();
        p.marryStatus = req.getMarryStatus();
        p.fMarryStatus = req.getFMarryStatus();

        // 2. 가족 & 자녀
        p.childCount = req.getChildCount();
        p.fChildCount = req.getFChildCount();
        p.isDoubleIncome = req.getIsDoubleIncome();
        p.fIsDoubleIncome = req.getFIsDoubleIncome();
        p.willContinueDoubleIncome = req.getWillContinueDoubleIncome();

        // 3. 집/부모
        p.currentDistrict = req.getCurrentDistrict();
        p.isHouseholder = req.getIsHouseholder();
        p.hasOwnedHouse = req.getHasOwnedHouse();
        p.unhousedStartYear = req.getUnhousedStartYear();
        p.isSupportingParents = req.getIsSupportingParents();
        p.fIsSupportingParents = req.getFIsSupportingParents();

        // 4. 돈 흐름
        p.jobTitle = req.getJobTitle();
        p.jobDistrict = req.getJobDistrict();
        p.annualIncome = req.getAnnualIncome();
        p.annualSideIncome = req.getAnnualSideIncome();
        p.monthlySavingAmount = req.getMonthlySavingAmount();
        p.currentFinancialAssets = req.getCurrentFinancialAssets();
        p.additionalAssets = req.getAdditionalAssets();
        p.targetSavingRate = req.getTargetSavingRate();

        p.hasDebt = req.getHasDebt();
        p.debtType = req.getDebtType();
        p.debtPrincipal = req.getDebtPrincipal();
        p.debtInterestRateBand = req.getDebtInterestRateBand();
        p.debtPrincipalPaid = req.getDebtPrincipalPaid();
        p.monthlyDebtPayment = req.getMonthlyDebtPayment();

        // 5. 청약 준비
        p.hasSubscriptionAccount = req.getHasSubscriptionAccount();
        p.subscriptionStartDate = req.getSubscriptionStartDate();
        p.fSubscriptionStartDate = req.getFSubscriptionStartDate();
        p.monthlySubscriptionAmount = req.getMonthlySubscriptionAmount();
        p.totalSubscriptionBalance = req.getTotalSubscriptionBalance();

        // 6. 살고 싶은 집
        p.targetSubscriptionType = req.getTargetSubscriptionType();
        p.preferredRegion = req.getPreferredRegion();

        // priorityCriteria : List<String> → "transport,school,price" 형태로 저장
        List<String> priorities = req.getPriorityCriteria();
        if (priorities != null && !priorities.isEmpty()) {
            p.priorityCriteria = String.join(",", priorities);
        } else {
            p.priorityCriteria = null;
        }

        p.preferredHousingSize = req.getPreferredHousingSize();

        return p;
    }
}
