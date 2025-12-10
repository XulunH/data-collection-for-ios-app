package com.xulunh.datacollectionforiosapp.controller;

import com.xulunh.datacollectionforiosapp.model.AnalyticsMetrics;
import com.xulunh.datacollectionforiosapp.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/daily")
    public AnalyticsMetrics getDailyMetrics(@RequestParam(required = false) String date) {
        LocalDate targetDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        return analyticsService.calculateDailyMetrics(targetDate);
    }

    @GetMapping("/summary")
    public Map<String, Object> getAnalyticsSummary() {
        AnalyticsMetrics metrics = analyticsService.calculateDailyMetrics(LocalDate.now());
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("date", metrics.getDate());
        summary.put("totalUsers", metrics.getTotalUsers());
        summary.put("dailyActiveUsers", metrics.getDailyActiveUsers());
        summary.put("weeklyActiveUsers", metrics.getWeeklyActiveUsers());
        summary.put("monthlyActiveUsers", metrics.getMonthlyActiveUsers());
        summary.put("newUsers", metrics.getNewUsers());
        summary.put("weeklyNewUsers", metrics.getWeeklyNewUsers());
        summary.put("totalMessages", metrics.getTotalMessages());
        summary.put("userMessages", metrics.getUserMessages());
        summary.put("assistantMessages", metrics.getAssistantMessages());
        summary.put("averageMessageLength", metrics.getAverageMessageLength());
        summary.put("onboardingCompletionRate", metrics.getOnboardingCompletionRate());
        summary.put("topMbtiType", metrics.getTopMbtiType());
        summary.put("calculatedAt", metrics.getCalculatedAt());
        
        return summary;
    }

    @GetMapping("/users")
    public Map<String, Object> getUserAnalytics() {
        AnalyticsMetrics metrics = analyticsService.calculateDailyMetrics(LocalDate.now());
        
        Map<String, Object> userAnalytics = new HashMap<>();
        userAnalytics.put("totalUsers", metrics.getTotalUsers());
        userAnalytics.put("dailyActiveUsers", metrics.getDailyActiveUsers());
        userAnalytics.put("weeklyActiveUsers", metrics.getWeeklyActiveUsers());
        userAnalytics.put("monthlyActiveUsers", metrics.getMonthlyActiveUsers());
        userAnalytics.put("newUsers", metrics.getNewUsers());
        userAnalytics.put("userRetentionRate", metrics.getUserRetentionRate());
        userAnalytics.put("onboardingCompletionRate", metrics.getOnboardingCompletionRate());
        userAnalytics.put("topMbtiType", metrics.getTopMbtiType());
        userAnalytics.put("mbtiUsersCount", metrics.getMbtiUsersCount());
        
        return userAnalytics;
    }

    @GetMapping("/messages")
    public Map<String, Object> getMessageAnalytics() {
        AnalyticsMetrics metrics = analyticsService.calculateDailyMetrics(LocalDate.now());
        
        Map<String, Object> messageAnalytics = new HashMap<>();
        messageAnalytics.put("totalMessages", metrics.getTotalMessages());
        messageAnalytics.put("userMessages", metrics.getUserMessages());
        messageAnalytics.put("assistantMessages", metrics.getAssistantMessages());
        messageAnalytics.put("memoryMessages", metrics.getMemoryMessages());
        messageAnalytics.put("averageMessageLength", metrics.getAverageMessageLength());
        messageAnalytics.put("totalSessions", metrics.getTotalSessions());
        messageAnalytics.put("averageSessionLength", metrics.getAverageSessionLength());
        messageAnalytics.put("averageMessagesPerSession", metrics.getAverageMessagesPerSession());
        
        return messageAnalytics;
    }

    @GetMapping("/mbti-distribution")
    public Map<String, Long> getMbtiDistribution() {
        return analyticsService.getMbtiDistribution();
    }

    @GetMapping("/time-distribution/hour")
    public Map<String, Long> getMessagesByHour() {
        return analyticsService.getMessagesByHour();
    }

    @GetMapping("/time-distribution/day")
    public Map<String, Long> getMessagesByDayOfWeek() {
        return analyticsService.getMessagesByDayOfWeek();
    }

    @GetMapping("/")
    public Map<String, String> analytics() {
        return Map.of(
            "status", "Analytics API is running",
            "message", "Use /api/analytics/summary for quick overview",
            "endpoints", "/daily, /summary, /users, /messages, /mbti-distribution, /time-distribution/hour, /time-distribution/day"
        );
    }
}
