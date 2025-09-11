package com.xulunh.datacollectionforiosapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    
    @Id
    private String id;
    
    @Field("user_id")
    private String userId;
    
    @Field("receiver_id")
    private String receiverId;
    
    @Field("content")
    private String content;
    
    @Field("role")
    private String role; // "user", "assistant", "memory"
    
    @Field("timestamp")
    private LocalDateTime timestamp;
    
    @Field("timestamp_local")
    private String timestampLocal;
    
    @Field("event_id")
    private String eventId;
}
