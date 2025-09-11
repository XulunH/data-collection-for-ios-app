package com.xulunh.datacollectionforiosapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/databases")
    public Map<String, Object> checkDatabases() {
        Map<String, Object> result = new HashMap<>();
        
        // Test MySQL connection
        try {
            String mysqlVersion = jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
            result.put("mysql", Map.of(
                "status", "connected",
                "version", mysqlVersion
            ));
        } catch (Exception e) {
            result.put("mysql", Map.of(
                "status", "error",
                "error", e.getMessage()
            ));
        }

        // Test MongoDB connection
        try {
            String mongoVersion = mongoTemplate.getDb().runCommand(org.bson.Document.parse("{buildInfo: 1}")).getString("version");
            result.put("mongodb", Map.of(
                "status", "connected",
                "version", mongoVersion
            ));
        } catch (Exception e) {
            result.put("mongodb", Map.of(
                "status", "error",
                "error", e.getMessage()
            ));
        }

        return result;
    }

    @GetMapping("/")
    public Map<String, String> health() {
        return Map.of(
            "status", "healthy",
            "service", "Synai Analytics Service",
            "message", "Database connections configured"
        );
    }
}
