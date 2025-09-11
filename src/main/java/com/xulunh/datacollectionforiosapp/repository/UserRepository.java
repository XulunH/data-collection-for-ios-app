package com.xulunh.datacollectionforiosapp.repository;

import com.xulunh.datacollectionforiosapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    // Count total users
    long count();
    
    // Count active users
    long countByIsActiveTrue();
    
    // Count new users by date range
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate AND u.createdAt < :endDate")
    long countNewUsersByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Count users by MBTI type
    @Query("SELECT u.mbti, COUNT(u) FROM User u WHERE u.mbti IS NOT NULL GROUP BY u.mbti ORDER BY COUNT(u) DESC")
    List<Object[]> countUsersByMbti();
    
    // Count onboarding completion rate
    @Query("SELECT COUNT(u) FROM User u WHERE u.onboardingDone = true")
    long countOnboardingCompleted();
    
    // Get users created in last N days
    @Query("SELECT u FROM User u WHERE u.createdAt >= :since")
    List<User> findUsersCreatedSince(@Param("since") LocalDateTime since);
}
