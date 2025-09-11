package com.xulunh.datacollectionforiosapp.controller;

import com.xulunh.datacollectionforiosapp.model.User;
import com.xulunh.datacollectionforiosapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/count")
    public Map<String, Object> getUserCounts() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("totalUsers", userRepository.count());
        result.put("activeUsers", userRepository.countByIsActiveTrue());
        result.put("onboardingCompleted", userRepository.countOnboardingCompleted());
        
        return result;
    }

    @GetMapping("/users/mbti")
    public List<Object[]> getMbtiDistribution() {
        return userRepository.countUsersByMbti();
    }

    @GetMapping("/")
    public Map<String, String> test() {
        return Map.of(
            "status", "test endpoint working",
            "message", "Ready to test database connections"
        );
    }
}
