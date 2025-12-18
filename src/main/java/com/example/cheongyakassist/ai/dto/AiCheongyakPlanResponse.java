package com.example.cheongyakassist.ai.dto;

import lombok.Data;
import java.util.List;

@Data
public class AiCheongyakPlanResponse {
    private Summary summary;
    private Diagnosis diagnosis;
    private TimeHorizonStrategy timeHorizonStrategy;
    private ChartData chartData;
    private PlanMeta planMeta;
    private String report;

    @Data
    public static class Summary {
        private String title;
        private String body;
    }

    @Data
    public static class Diagnosis {
        private Boolean canBuyWithCheongyak;
        private String confidenceLevel;  // "HIGH", "MEDIUM", "LOW"
        private List<String> reasons;
    }

    @Data
    public static class TimeHorizonStrategy {
        private String now;
        private String threeYears;
        private String fiveYears;
    }

    @Data
    public static class ChartData {
        private List<SavingProjection> savingProjectionByYear;
    }

    @Data
    public static class SavingProjection {
        private Integer year;
        private Integer amount;
    }

    @Data
    public static class PlanMeta {
        private String recommendedHorizon;  // "SHORT_3", "MID_5", "LONG_10"
        private String reason;
    }
}