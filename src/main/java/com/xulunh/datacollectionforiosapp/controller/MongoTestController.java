package com.xulunh.datacollectionforiosapp.controller;

import com.xulunh.datacollectionforiosapp.model.Message;
import com.xulunh.datacollectionforiosapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mongo-test")
public class MongoTestController {

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/create-test-message")
    public Map<String, Object> createTestMessage() {
        // Create a test message
        Message testMessage = new Message();
        testMessage.setUserId("user-001");
        testMessage.setReceiverId("model-1");
        testMessage.setContent("Hello, this is a test message!");
        testMessage.setRole("user");
        testMessage.setTimestamp(LocalDateTime.now());
        testMessage.setTimestampLocal(LocalDateTime.now().toString());
        
        Message savedMessage = messageRepository.save(testMessage);
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "Test message created");
        result.put("savedMessage", savedMessage);
        
        return result;
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/messages/count")
    public Map<String, Object> getMessageCounts() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("totalMessages", messageRepository.count());
        result.put("userMessages", messageRepository.countByRole("user"));
        result.put("assistantMessages", messageRepository.countByRole("assistant"));
        result.put("memoryMessages", messageRepository.countByRole("memory"));
        
        return result;
    }

    @GetMapping("/")
    public Map<String, String> test() {
        return Map.of(
            "status", "MongoDB test endpoint working",
            "message", "Ready to test MongoDB connections"
        );
    }
}
