package com.xulunh.datacollectionforiosapp.controller;

import com.xulunh.datacollectionforiosapp.model.Message;
import com.xulunh.datacollectionforiosapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/test-data")
public class TestDataController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/create-sample-messages")
    @PostMapping("/create-sample-messages")
    public Map<String, Object> createSampleMessages() {
        List<Message> messages = new ArrayList<>();
        LocalDateTime baseTime = LocalDateTime.now().minusHours(2);

        // User 1 (Alice) - ENFP - Active user with multiple messages
        messages.add(createMessage("user-001", "model-1", "Hi Mindee, I'm feeling really anxious about my job interview tomorrow. Any advice?", "user", baseTime.plusMinutes(5)));
        messages.add(createMessage("user-001", "model-1", "Don't worry, Alice! You've got this! Let's break down what's making you anxious. What specific part of the interview is worrying you most?", "assistant", baseTime.plusMinutes(6)));
        messages.add(createMessage("user-001", "model-1", "I'm worried about the technical questions. What if I don't know the answer?", "user", baseTime.plusMinutes(8)));
        messages.add(createMessage("user-001", "model-1", "That's totally normal! Remember, it's okay to say 'I don't know, but here's how I would approach finding the answer.' This shows problem-solving skills!", "assistant", baseTime.plusMinutes(9)));

        // User 2 (Bob) - INTJ - Quick interaction
        messages.add(createMessage("user-002", "model-1", "I need help organizing my thoughts for a presentation.", "user", baseTime.plusMinutes(15)));
        messages.add(createMessage("user-002", "model-1", "I'd be happy to help! What's the presentation about and who's your audience?", "assistant", baseTime.plusMinutes(16)));
        messages.add(createMessage("user-002", "model-1", "It's about our Q4 results for the board. I want to make it data-driven but engaging.", "user", baseTime.plusMinutes(18)));
        messages.add(createMessage("user-002", "model-1", "Perfect! Let's structure it with a clear narrative: Challenge → Solution → Results → Next Steps. What's your biggest win this quarter?", "assistant", baseTime.plusMinutes(19)));

        // User 3 (Charlie) - ESFP - Emotional support
        messages.add(createMessage("user-003", "model-1", "I had a really bad day at work today. My boss criticized my project in front of everyone.", "user", baseTime.plusMinutes(25)));
        messages.add(createMessage("user-003", "model-1", "I'm so sorry that happened, Charlie. That must have felt really embarrassing and hurtful. How are you feeling right now?", "assistant", baseTime.plusMinutes(26)));
        messages.add(createMessage("user-003", "model-1", "I feel like I'm not good enough. Maybe I should just quit.", "user", baseTime.plusMinutes(28)));
        messages.add(createMessage("user-003", "model-1", "Those feelings are completely valid, but remember: one bad moment doesn't define your worth or your abilities. Let's talk about what you're proud of in your work.", "assistant", baseTime.plusMinutes(29)));

        // User 4 (Diana) - INFJ - Deep conversation
        messages.add(createMessage("user-004", "model-1", "I've been thinking a lot about my purpose in life lately. How do people figure out what they're meant to do?", "user", baseTime.plusMinutes(35)));
        messages.add(createMessage("user-004", "model-1", "That's such a profound question, Diana. Purpose often emerges from the intersection of what you're good at, what you care about, and what the world needs. What activities make you lose track of time?", "assistant", baseTime.plusMinutes(36)));
        messages.add(createMessage("user-004", "model-1", "I love helping people solve problems, especially complex ones. And I'm fascinated by human psychology.", "user", baseTime.plusMinutes(38)));
        messages.add(createMessage("user-004", "model-1", "That sounds like a beautiful combination! Have you considered careers in counseling, coaching, or organizational psychology? Your natural empathy and analytical mind could be powerful tools for helping others.", "assistant", baseTime.plusMinutes(39)));

        // User 5 (Eve) - ESTJ - Practical advice
        messages.add(createMessage("user-005", "model-1", "I need to improve my team's productivity. We're missing deadlines and the quality is suffering.", "user", baseTime.plusMinutes(45)));
        messages.add(createMessage("user-005", "model-1", "Let's tackle this systematically, Eve. What's the biggest bottleneck you're seeing? Is it process, resources, or communication?", "assistant", baseTime.plusMinutes(46)));
        messages.add(createMessage("user-005", "model-1", "I think it's communication. People aren't clear on priorities and there's too much back-and-forth.", "user", baseTime.plusMinutes(48)));
        messages.add(createMessage("user-005", "model-1", "Great diagnosis! Consider implementing daily stand-ups, clear project briefs, and a shared task management system. Would you like me to help you design a communication framework?", "assistant", baseTime.plusMinutes(49)));

        // Add some memory events
        messages.add(createMessage("user-001", "model-1", "🧠 Memory Event: Alice's job interview anxiety - ongoing support provided, confidence building techniques shared", "memory", baseTime.plusMinutes(10)));
        messages.add(createMessage("user-003", "model-1", "🧠 Memory Event: Charlie's workplace confidence issue - emotional support provided, self-worth discussion initiated", "memory", baseTime.plusMinutes(30)));

        // Save all messages
        List<Message> savedMessages = messageRepository.saveAll(messages);

        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "Sample messages created successfully");
        result.put("totalMessages", savedMessages.size());
        result.put("userMessages", savedMessages.stream().filter(m -> "user".equals(m.getRole())).count());
        result.put("assistantMessages", savedMessages.stream().filter(m -> "assistant".equals(m.getRole())).count());
        result.put("memoryMessages", savedMessages.stream().filter(m -> "memory".equals(m.getRole())).count());
        result.put("uniqueUsers", savedMessages.stream().map(Message::getUserId).distinct().count());

        return result;
    }

    @PostMapping("/create-historical-messages")
    public Map<String, Object> createHistoricalMessages() {
        List<Message> messages = new ArrayList<>();
        LocalDateTime baseTime = LocalDateTime.now().minusDays(7);

        // Create messages from the past week to show historical data
        for (int day = 0; day < 7; day++) {
            LocalDateTime dayTime = baseTime.plusDays(day);
            
            // Each day, 2-3 users have conversations
            String[] users = {"user-001", "user-002", "user-003", "user-004", "user-005"};
            int usersPerDay = 2 + (day % 3); // 2-4 users per day
            
            for (int i = 0; i < usersPerDay; i++) {
                String userId = users[i % users.length];
                LocalDateTime conversationTime = dayTime.plusHours(9 + (i * 2)); // Spread throughout the day
                
                // Create a short conversation
                messages.add(createMessage(userId, "model-1", "Good morning! How are you today?", "user", conversationTime));
                messages.add(createMessage(userId, "model-1", "Good morning! I'm doing well, thank you for asking. How can I help you today?", "assistant", conversationTime.plusMinutes(1)));
                messages.add(createMessage(userId, "model-1", "I'm feeling a bit stressed about work. Can you help me organize my thoughts?", "user", conversationTime.plusMinutes(3)));
                messages.add(createMessage(userId, "model-1", "Of course! Let's break this down together. What's the main source of your work stress right now?", "assistant", conversationTime.plusMinutes(4)));
            }
        }

        List<Message> savedMessages = messageRepository.saveAll(messages);

        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "Historical messages created successfully");
        result.put("totalMessages", savedMessages.size());
        result.put("daysCovered", 7);
        result.put("uniqueUsers", savedMessages.stream().map(Message::getUserId).distinct().count());

        return result;
    }

    @DeleteMapping("/clear-all-messages")
    public Map<String, Object> clearAllMessages() {
        long count = messageRepository.count();
        messageRepository.deleteAll();
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "All messages cleared");
        result.put("deletedCount", count);
        
        return result;
    }

    @GetMapping("/message-stats")
    public Map<String, Object> getMessageStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMessages", messageRepository.count());
        stats.put("userMessages", messageRepository.countByRole("user"));
        stats.put("assistantMessages", messageRepository.countByRole("assistant"));
        stats.put("memoryMessages", messageRepository.countByRole("memory"));
        
        // Get unique users
        List<Message> allMessages = messageRepository.findAll();
        long uniqueUsers = allMessages.stream().map(Message::getUserId).distinct().count();
        stats.put("uniqueUsers", uniqueUsers);
        
        return stats;
    }

    private Message createMessage(String userId, String receiverId, String content, String role, LocalDateTime timestamp) {
        Message message = new Message();
        message.setUserId(userId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setRole(role);
        message.setTimestamp(timestamp);
        message.setTimestampLocal(timestamp.toString());
        return message;
    }
}
