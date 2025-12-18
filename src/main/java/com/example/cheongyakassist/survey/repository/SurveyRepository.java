package com.example.cheongyakassist.survey.repository;

import com.example.cheongyakassist.survey.entity.HousingProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<HousingProfile, Long> {
}
