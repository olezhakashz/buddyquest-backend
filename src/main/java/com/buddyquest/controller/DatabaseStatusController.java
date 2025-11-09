package com.buddyquest.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DatabaseStatusController {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseStatusController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/db/status")
    public Map<String, Object> dbStatus() {
        Integer one = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        return Map.of(
                "database", one != null && one == 1 ? "connected" : "error"
        );
    }

    // Доп. проверка — попробуем дернуть текущее время из БД
    @GetMapping("/db/time")
    public Map<String, Object> dbTime() {
        String now = jdbcTemplate.queryForObject("SELECT NOW()::text", String.class);
        return Map.of("now", now);
    }
}

