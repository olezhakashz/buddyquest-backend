package com.buddyquest.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/db")
public class DatabaseStatusController {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseStatusController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        Integer one = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        return Map.of("database", one != null && one == 1 ? "connected" : "error");
    }
}

