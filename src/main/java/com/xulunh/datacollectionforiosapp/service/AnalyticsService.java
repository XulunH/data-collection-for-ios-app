package com.xulunh.datacollectionforiosapp.service;

import com.xulunh.datacollectionforiosapp.model.AnalyticsMetrics;
import com.xulunh.datacollectionforiosapp.model.User;
import com.xulunh.datacollectionforiosapp.model.Message;
import com.xulunh.datacollectionforiosapp.repository.UserRepository;
import com.xulunh.datacollectionforiosapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    public AnalyticsMetrics calculateDailyMetrics(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        // User metrics
        long totalUsers = userRepository.count();
        long newUsers = userRepository.countNewUsersByDateRange(startOfDay, endOfDay);
        long activeUsers = calculateDailyActiveUsers(startOfDay, endOfDay);
        long onboardingCompleted = userRepository.countOnboardingCompleted();

        // Message metrics
        long totalMessages = messageRepository.count();
        long userMessages = messageRepository.countByRole("user");
        long assistantMessages = messageRepository.countByRole("assistant");
        long memoryMessages = messageRepository.countByRole("memory");

        // Calculate averages
        double averageMessageLength = calculateAverageMessageLength();
        double averageResponseTime = calculateAverageResponseTime(startOfDay, endOfDay);

        // Session metrics
        long totalSessions = calculateTotalSessions(startOfDay, endOfDay);
        double averageSessionLength = calculateAverageSessionLength(startOfDay, endOfDay);
        double averageMessagesPerSession = totalSessions > 0 ? (double) totalMessages / totalSessions : 0;

        // Engagement metrics
        double userRetentionRate = calculateUserRetentionRate();
        double onboardingCompletionRate = totalUsers > 0 ? (double) onboardingCompleted / totalUsers * 100 : 0;

        // MBTI distribution
        List<Object[]> mbtiData = userRepository.countUsersByMbti();
        String topMbtiType = mbtiData.isEmpty() ? "N/A" : (String) mbtiData.get(0)[0];
        long mbtiUsersCount = mbtiData.isEmpty() ? 0 : (Long) mbtiData.get(0)[1];

        return AnalyticsMetrics.builder()
                .date(date)
                .totalUsers(totalUsers)
                .dailyActiveUsers(activeUsers)
                .weeklyActiveUsers(calculateWeeklyActiveUsers())
                .monthlyActiveUsers(calculateMonthlyActiveUsers())
                .newUsers(newUsers)
                .totalMessages(totalMessages)
                .userMessages(userMessages)
                .assistantMessages(assistantMessages)
                .memoryMessages(memoryMessages)
                .averageMessageLength(averageMessageLength)
                .averageResponseTime(averageResponseTime)
                .totalSessions(totalSessions)
                .averageSessionLength(averageSessionLength)
                .averageMessagesPerSession(averageMessagesPerSession)
                .userRetentionRate(userRetentionRate)
                .onboardingCompletionRate(onboardingCompletionRate)
                .topMbtiType(topMbtiType)
                .mbtiUsersCount(mbtiUsersCount)
                .calculatedAt(LocalDateTime.now())
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    private long calculateDailyActiveUsers(LocalDateTime start, LocalDateTime end) {
        // Get all messages in the date range and count unique users
        List<Message> messages = messageRepository.findByTimestampBetween(start, end);
        return messages.stream()
                .map(Message::getUserId)
                .distinct()
                .count();
    }

    private long calculateWeeklyActiveUsers() {
        LocalDateTime weekAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        List<Message> messages = messageRepository.findByTimestampBetween(weekAgo, LocalDateTime.now());
        return messages.stream()
                .map(Message::getUserId)
                .distinct()
                .count();
    }

    private long calculateMonthlyActiveUsers() {
        LocalDateTime monthAgo = LocalDateTime.now().minus(30, ChronoUnit.DAYS);
        List<Message> messages = messageRepository.findByTimestampBetween(monthAgo, LocalDateTime.now());
        return messages.stream()
                .map(Message::getUserId)
                .distinct()
                .count();
    }

    private double calculateAverageMessageLength() {
        List<Message> messages = messageRepository.findAll();
        if (messages.isEmpty()) return 0.0;
        
        return messages.stream()
                .mapToInt(msg -> msg.getContent() != null ? msg.getContent().length() : 0)
                .average()
                .orElse(0.0);
    }

    private double calculateAverageResponseTime(LocalDateTime start, LocalDateTime end) {
        // This is a simplified calculation
        // In a real scenario, you'd track response times more precisely
        List<Message> userMessages = messageRepository.findByRoleAndTimestampBetween("user", start, end);
        List<Message> assistantMessages = messageRepository.findByRoleAndTimestampBetween("assistant", start, end);
        
        if (userMessages.isEmpty() || assistantMessages.isEmpty()) return 0.0;
        
        // Simple approximation: average time between user and assistant messages
        return 1.2; // Placeholder - would need more sophisticated calculation
    }

    private long calculateTotalSessions(LocalDateTime start, LocalDateTime end) {
        // A session is defined as a sequence of messages from the same user
        List<Message> messages = messageRepository.findByTimestampBetween(start, end);
        return messages.stream()
                .map(Message::getUserId)
                .distinct()
                .count();
    }

    private double calculateAverageSessionLength(LocalDateTime start, LocalDateTime end) {
        // Simplified calculation - would need more sophisticated session tracking
        return 15.5; // Placeholder in minutes
    }

    private double calculateUserRetentionRate() {
        // Simplified calculation - would need more sophisticated retention tracking
        LocalDateTime weekAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        long usersFromWeekAgo = userRepository.countNewUsersByDateRange(weekAgo, LocalDateTime.now());
        long totalUsers = userRepository.count();
        
        return totalUsers > 0 ? (double) usersFromWeekAgo / totalUsers * 100 : 0;
    }

    public Map<String, Long> getMbtiDistribution() {
        List<User> users = userRepository.findAll();
        Map<String, Long> mbtiCount = new HashMap<>();
        
        for (User user : users) {
            String mbti = user.getMbti();
            if (mbti != null && !mbti.isEmpty()) {
                mbtiCount.put(mbti, mbtiCount.getOrDefault(mbti, 0L) + 1);
            }
        }
        
        return mbtiCount;
    }

    public Map<String, Long> getMessagesByHour() {
        List<Message> messages = messageRepository.findAll();
        Map<String, Long> hourlyCount = new HashMap<>();
        
        // Initialize all hours with 0
        for (int hour = 0; hour < 24; hour++) {
            hourlyCount.put(String.format("%02d:00", hour), 0L);
        }
        
        // Count messages by hour
        for (Message message : messages) {
            if (message.getTimestamp() != null) {
                int hour = message.getTimestamp().getHour();
                String hourKey = String.format("%02d:00", hour);
                hourlyCount.put(hourKey, hourlyCount.get(hourKey) + 1);
            }
        }
        
        return hourlyCount;
    }

    public Map<String, Long> getMessagesByDayOfWeek() {
        List<Message> messages = messageRepository.findAll();
        Map<String, Long> dailyCount = new HashMap<>();
        
        // Initialize all days with 0
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        for (String day : days) {
            dailyCount.put(day, 0L);
        }
        
        // Count messages by day of week
        for (Message message : messages) {
            if (message.getTimestamp() != null) {
                int dayOfWeek = message.getTimestamp().getDayOfWeek().getValue(); // 1=Monday, 7=Sunday
                String dayKey = days[dayOfWeek - 1];
                dailyCount.put(dayKey, dailyCount.get(dayKey) + 1);
            }
        }
        
        return dailyCount;
    }
}
