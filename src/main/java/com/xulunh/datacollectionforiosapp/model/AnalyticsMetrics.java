package com.xulunh.datacollectionforiosapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsMetrics {
    
    // Date for these metrics
    private LocalDate date;
    
    // User metrics
    private Long totalUsers;
    private Long dailyActiveUsers;
    private Long weeklyActiveUsers;
    private Long monthlyActiveUsers;
    private Long newUsers;
    
    // Message metrics
    private Long totalMessages;
    private Long userMessages;
    private Long assistantMessages;
    private Long memoryMessages;
    private Double averageMessageLength;
    private Double averageResponseTime;
    
    // Session metrics
    private Long totalSessions;
    private Double averageSessionLength;
    private Double averageMessagesPerSession;
    
    // Engagement metrics
    private Double userRetentionRate;
    private Double onboardingCompletionRate;
    private Double returnUserRate;
    
    // MBTI distribution
    private String topMbtiType;
    private Long mbtiUsersCount;
    
    // Timestamps
    private LocalDateTime calculatedAt;
    private LocalDateTime lastUpdated;
}
