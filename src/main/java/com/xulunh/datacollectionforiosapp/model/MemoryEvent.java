package com.xulunh.datacollectionforiosapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "memory_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoryEvent {
    
    @Id
    private String id;
    
    @Field("event_id")
    private String eventId;
    
    @Field("user_id")
    private String userId;
    
    @Field("event_type")
    private String eventType;
    
    @Field("event_summary")
    private String eventSummary;
    
    @Field("event_status")
    private String eventStatus; // "ongoing", "resolved"
    
    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    @Field("updated_at_local")
    private String updatedAtLocal;
}
