package com.buddyquest.controller;

import com.buddyquest.dto.LeaderboardEntryResponse;
import com.buddyquest.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final UserService userService;

    public LeaderboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<LeaderboardEntryResponse> getLeaderboard() {
        return userService.getLeaderboard();
    }
}

