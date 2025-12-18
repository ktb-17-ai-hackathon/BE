package com.example.cheongyakassist.survey.service;

import com.example.cheongyakassist.survey.dto.SurveyCreateRequest;
import com.example.cheongyakassist.survey.dto.SurveyResponseDto;
import com.example.cheongyakassist.survey.entity.HousingProfile;
import com.example.cheongyakassist.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    @Transactional
    public Long createSurvey(SurveyCreateRequest req) {

        // DTO -> Entity 변환 (새 설문 구조 기준)
        HousingProfile profile = HousingProfile.from(req);

        HousingProfile saved = surveyRepository.save(profile);
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public SurveyResponseDto getSurvey(Long surveyId) {
        HousingProfile profile = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 설문입니다. id=" + surveyId));

        return SurveyResponseDto.builder()
                .surveyId(profile.getId())

                // 1. 기본 정보
                .age(profile.getAge())
                .marryStatus(profile.getMarryStatus())
                .fMarryStatus(profile.getFMarryStatus())

                // 2. 가족 & 자녀
                .childCount(profile.getChildCount())
                .fChildCount(profile.getFChildCount())
                .isDoubleIncome(profile.getIsDoubleIncome())
                .fIsDoubleIncome(profile.getFIsDoubleIncome())
                .willContinueDoubleIncome(profile.getWillContinueDoubleIncome())

                // 3. 집/부모
                .currentDistrict(profile.getCurrentDistrict())
                .isHouseholder(profile.getIsHouseholder())
                .hasOwnedHouse(profile.getHasOwnedHouse())
                .unhousedStartYear(profile.getUnhousedStartYear())
                .isSupportingParents(profile.getIsSupportingParents())
                .fIsSupportingParents(profile.getFIsSupportingParents())

                // 4. 돈 흐름
                .jobTitle(profile.getJobTitle())
                .jobDistrict(profile.getJobDistrict())
                .annualIncome(profile.getAnnualIncome())
                .annualSideIncome(profile.getAnnualSideIncome())
                .monthlySavingAmount(profile.getMonthlySavingAmount())
                .currentFinancialAssets(profile.getCurrentFinancialAssets())
                .additionalAssets(profile.getAdditionalAssets())
                .targetSavingRate(profile.getTargetSavingRate())
                .hasDebt(profile.getHasDebt())
                .debtType(profile.getDebtType())
                .debtPrincipal(profile.getDebtPrincipal())
                .debtInterestRateBand(profile.getDebtInterestRateBand())
                .debtPrincipalPaid(profile.getDebtPrincipalPaid())
                .monthlyDebtPayment(profile.getMonthlyDebtPayment())

                // 5. 청약 준비
                .hasSubscriptionAccount(profile.getHasSubscriptionAccount())
                .subscriptionStartDate(profile.getSubscriptionStartDate())
                .fSubscriptionStartDate(profile.getFSubscriptionStartDate())
                .monthlySubscriptionAmount(profile.getMonthlySubscriptionAmount())
                .totalSubscriptionBalance(profile.getTotalSubscriptionBalance())

                // 6. 살고 싶은 집/선호
                .targetSubscriptionType(profile.getTargetSubscriptionType())
                .preferredRegion(profile.getPreferredRegion())
                .priorityCriteria(profile.getPriorityCriteria())
                .preferredHousingSize(profile.getPreferredHousingSize())

                // 공통
                .createdAt(profile.getCreatedAt())
                .build();
    }
}
