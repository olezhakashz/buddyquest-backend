package com.buddyquest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/status")
public class StatusController {

    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of("status", "ok", "message", "pong");
    }

    @GetMapping
    public Map<String, Object> info() {
        return Map.of("status", "ok", "uptimeSeconds", (double) (System.nanoTime() / 1_000_000_000L));
    }
}
