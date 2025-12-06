package com.xulunh.datacollectionforiosapp.repository;

import com.xulunh.datacollectionforiosapp.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    
    // Count total messages
    long count();
    
    // Count messages by role
    long countByRole(String role);
    
    // Count messages by date range
    @Query("{'timestamp': {$gte: ?0, $lt: ?1}}")
    long countByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    // Find messages by date range
    @Query("{'timestamp': {$gte: ?0, $lt: ?1}}")
    List<Message> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    // Count messages by user and date range
    @Query("{'user_id': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}")
    long countByUserIdAndTimestampBetween(String userId, LocalDateTime start, LocalDateTime end);
    
    // Find messages by user
    List<Message> findByUserId(String userId);
    
    // Find messages by user and date range
    @Query("{'user_id': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}")
    List<Message> findByUserIdAndTimestampBetween(String userId, LocalDateTime start, LocalDateTime end);
    
    // Find messages by role and date range
    @Query("{'role': ?0, 'timestamp': {$gte: ?1, $lt: ?2}}")
    List<Message> findByRoleAndTimestampBetween(String role, LocalDateTime start, LocalDateTime end);
    
    // Get unique user IDs from messages
    @Query(value = "{}", fields = "{'user_id': 1}")
    List<Message> findDistinctUserIds();
    
    // Count messages per user
    @Query(value = "{}", fields = "{'user_id': 1}")
    List<Object> countMessagesPerUser();
}
